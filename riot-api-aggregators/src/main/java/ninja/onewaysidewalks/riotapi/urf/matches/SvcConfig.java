package ninja.onewaysidewalks.riotapi.urf.matches;

import io.dropwizard.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ninja.onewaysidewalks.cassandra.client.Config;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.messaging.client.consumers.CompetingConsumerConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class SvcConfig extends Configuration implements ConfigWithCassandra {
    private CompetingConsumerConfig messagingConsumer = new CompetingConsumerConfig();

    private Config cassandraConfig;
}
