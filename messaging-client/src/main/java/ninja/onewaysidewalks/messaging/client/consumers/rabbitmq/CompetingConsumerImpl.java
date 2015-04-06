package ninja.onewaysidewalks.messaging.client.consumers.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;
import ninja.onewaysidewalks.messaging.client.consumers.Consumer;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.utilities.ScheduledThreadExecutor;

import java.io.IOException;

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

        channel.queueDeclare(consumerConfig.getQueue(), true, false, false, null);

        channel.exchangeDeclare(consumerConfig.getTopic(), "fanout");
        channel.queueBind(consumerConfig.getQueue(), consumerConfig.getTopic(), "");

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

                        if (! uncheckedMessage.getClass().isAssignableFrom(messageClass)) {
                            throw new RuntimeException("Message serialization doesnt match handler");
                        }

                        T message = (T) uncheckedMessage; //unchecked cast? TODO: find a better solution to the dynamic type checking

                        //forward onto handler.
                        messageHandler.handleMessage(message);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (Exception ex) {
                        log.error("couldnt consume and ack message", ex);
                        throw new RuntimeException(ex);
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
