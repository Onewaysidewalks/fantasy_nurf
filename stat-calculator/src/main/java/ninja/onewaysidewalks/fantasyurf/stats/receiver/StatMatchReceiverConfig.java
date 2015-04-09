package ninja.onewaysidewalks.fantasyurf.stats.receiver;

import io.dropwizard.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ninja.onewaysidewalks.cassandra.client.Config;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatMatchReceiverConfig extends Configuration implements ConfigWithCassandra {
    private Config cassandraConfig;

    private CompetingConsumerConfig messagingConsumer;
}
