package ninja.onewaysidewalks.messaging.client.consumers.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;
import ninja.onewaysidewalks.messaging.client.consumers.Consumer;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.utilities.ScheduledThreadExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CompetingConsumerImpl<T> implements Consumer<T> {
    private final CompetingConsumerConfig consumerConfig;
    private Connection connection;
    private Channel channel;
    private final MessageHandler<T> messageHandler;
    private final Class<?> messageClass;
    private ScheduledThreadExecutor scheduledThreadExecutor;
    private final ObjectMapper objectMapper;

    public CompetingConsumerImpl(ObjectMapper objectMapper, CompetingConsumerConfig consumerConfig, MessageHandler<T> messageHandler) {
        this.consumerConfig = consumerConfig;
        this.messageHandler = messageHandler;
        this.messageClass = messageHandler.getMessageType();
        this.objectMapper = objectMapper;
    }

    @Override
    public void start() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(consumerConfig.getHost());

        if (consumerConfig.getUsername() != null
                && consumerConfig.getPassword() != null) {
            connectionFactory.setUsername(consumerConfig.getUsername());
            connectionFactory.setPassword(consumerConfig.getPassword());
        }

        connection = connectionFactory.newConnection();

        channel = connection.createChannel();

        //Setup exchanges if they dont already exist
        channel.exchangeDeclare(consumerConfig.getTopic(), "fanout");
        String dlqTopic = String.format("%s.dlq", consumerConfig.getTopic());
        channel.exchangeDeclare(dlqTopic, "fanout");


        //Declare queue and bind it to the topic
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlqTopic);//Setup Dead Letter Exchange forwarding, if it doesnt already exist
        channel.queueDeclare(consumerConfig.getQueue(), true, false, false, args);
        channel.queueBind(consumerConfig.getQueue(), consumerConfig.getTopic(), "");

        //Declare DLQ and bind it to the DLQ topic, to prevent messages from disappearing
        channel.queueDeclare(consumerConfig.getQueue() + ".dlq", true, false, false, null);
        channel.queueBind(consumerConfig.getQueue() + ".dlq", dlqTopic, "");

        final QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(consumerConfig.getQueue(), false, consumer);

        //TODO: concurrent consumers?
        //TODO: timeouts?
        scheduledThreadExecutor = new ScheduledThreadExecutor(1, false);
        scheduledThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    try {

                        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                        Object uncheckedMessage = objectMapper.readValue(delivery.getBody(), messageClass);

                        if (!uncheckedMessage.getClass().isAssignableFrom(messageClass)) {
                            throw new RuntimeException("Message serialization doesnt match handler");
                        }

                        T message = (T) uncheckedMessage; //unchecked cast? TODO: find a better solution to the dynamic type checking

                        //forward onto handler.
                        try {
                            messageHandler.handleMessage(message);
                        } catch (RuntimeException re) {
                            log.error("couldnt process message: {}", message);
                            //Message should be unack'd to go to another consumer if it hasnt been redelivered.
                            //if it has been redelivered, it should be deadlettered
                            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, !delivery.getEnvelope().isRedeliver());
                        }

                        try {
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (AlreadyClosedException ace) {
                            //Something went wrong with the channel
                            //Restart it
                            channel = connection.createChannel();
                            channel.basicConsume(consumerConfig.getQueue(), false, consumer);
                        }
                    } catch (Exception ex) {
                        log.error("couldnt consume and ack message", ex);
                    }
                }
            }
        });
    }

    @Override
    public void stop() throws IOException {
        scheduledThreadExecutor.shutdown();

        if (channel != null) {
            channel.close();
        }

        if (connection != null) {
            connection.close();
        }
    }
}
