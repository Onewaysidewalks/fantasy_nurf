package ninja.onewaysidewalks.fantasyurf.stats.supervisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ninja.onewaysidewalks.fantasyurf.stats.worker.StatCalculatorWorkerConfig;
import ninja.onewaysidewalks.messaging.client.producers.Producer;
import ninja.onewaysidewalks.messaging.client.producers.rabbitmq.fanout.FanoutProducerImpl;

public class ProducerModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public Producer getProducer(StatCalculatorWorkerConfig config, ObjectMapper objectMapper) {
        return new FanoutProducerImpl(objectMapper, config.getMessagingProducer());
    }
}
