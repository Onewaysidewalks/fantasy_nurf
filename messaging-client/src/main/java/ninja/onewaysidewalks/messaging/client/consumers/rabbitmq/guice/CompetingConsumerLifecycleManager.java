package ninja.onewaysidewalks.messaging.client.consumers.rabbitmq.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import io.dropwizard.lifecycle.Managed;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;
import ninja.onewaysidewalks.messaging.client.  consumers.MessageHandler;
import ninja.onewaysidewalks.messaging.client.consumers.rabbitmq.CompetingConsumerImpl;

public class CompetingConsumerLifecycleManager<T> implements Managed {

    private final Injector injector;
    private final CompetingConsumerConfig competingConsumerConfig;
    private final Class<? extends MessageHandler<T>> messageHandlerClass;

    private CompetingConsumerImpl<T> consumer;

    public CompetingConsumerLifecycleManager(
            CompetingConsumerConfig config,
            Injector injector,
            Class<? extends MessageHandler<T>> messageHandlerClass) {
        this.competingConsumerConfig = config;
        this.injector = injector;
        this.messageHandlerClass = messageHandlerClass;
    }

    @Override
    public void start() throws Exception {
        consumer = new CompetingConsumerImpl<T>(
                injector.getInstance(ObjectMapper.class),
                competingConsumerConfig, injector.getInstance(messageHandlerClass));

        consumer.start();
    }

    @Override
    public void stop() throws Exception {
        consumer.stop();
    }
}
