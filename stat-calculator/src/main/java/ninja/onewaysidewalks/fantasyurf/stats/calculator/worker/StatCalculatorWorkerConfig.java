package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker;

import io.dropwizard.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ninja.onewaysidewalks.cassandra.client.Config;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;
import ninja.onewaysidewalks.messaging.client.producers.ProducerConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatCalculatorWorkerConfig extends Configuration implements ConfigWithCassandra {
    private Config cassandraConfig;

    private CompetingConsumerConfig messagingConsumer;

    private String matchApiUrl;

    private ProducerConfig messagingProducer;
}
