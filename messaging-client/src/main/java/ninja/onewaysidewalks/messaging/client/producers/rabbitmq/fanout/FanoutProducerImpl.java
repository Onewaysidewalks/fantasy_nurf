package ninja.onewaysidewalks.messaging.client.producers.rabbitmq.fanout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.producers.Producer;
import ninja.onewaysidewalks.messaging.client.producers.ProducerConfig;
import ninja.onewaysidewalks.messaging.client.producers.ProducerSendException;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FanoutProducerImpl implements Producer {
    private final ProducerConfig producerConfig;

    private Connection connection;
    private Channel channel;
    private final ObjectMapper objectMapper;

    private Set<String> exchangeCreatedClass;

    public FanoutProducerImpl(ObjectMapper objectMapper, ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
        this.objectMapper = objectMapper;
        this.exchangeCreatedClass = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()); //Oh java.

        initialize();

    }

    public void initialize() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();

            connectionFactory.setHost(producerConfig.getHost());
            if (producerConfig.getPassword() != null
                    && producerConfig.getUsername() != null) {
                connectionFactory.setUsername(producerConfig.getUsername());
                connectionFactory.setPassword(producerConfig.getPassword());
            }

            connection = connectionFactory.newConnection();

            channel = connection.createChannel();
        } catch (IOException ioe) {
            log.error("unable to create messaging setup: {}", ioe);
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public <T> void sendMessage(String exchange, T message) {
        try {
            if (!exchangeCreatedClass.contains(exchange)) {
                channel.exchangeDeclare(exchange, "fanout");
                exchangeCreatedClass.add(exchange);
            }

            channel.basicPublish(exchange, "", true, false, MessageProperties.PERSISTENT_TEXT_PLAIN, objectMapper.writeValueAsBytes(message));
        } catch (IOException ioe) {
            log.error("error while trying to send to exchange {}, message: {}", exchange, message, ioe);
            throw new ProducerSendException(ioe);
        }
    }

    public void tearDown() throws IOException {
        connection.close();
        channel.close();
    }
}
