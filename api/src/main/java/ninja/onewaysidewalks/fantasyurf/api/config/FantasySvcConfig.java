package ninja.onewaysidewalks.fantasyurf.api.config;

import io.dropwizard.Configuration;
import lombok.Getter;
import ninja.onewaysidewalks.cassandra.client.Config;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;

public class FantasySvcConfig extends Configuration implements ConfigWithCassandra {

    @Getter
    private Config cassandraConfig;
}
