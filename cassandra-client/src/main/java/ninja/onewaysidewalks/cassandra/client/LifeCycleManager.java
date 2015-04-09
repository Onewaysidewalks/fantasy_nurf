package ninja.onewaysidewalks.cassandra.client;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.lifecycle.Managed;
import ninja.onewaysidewalks.cassandra.client.guice.CassandraModule;

import javax.inject.Provider;

public class LifeCycleManager<T extends ConfigWithCassandra> implements Managed {

    private final Provider<ConfigWithCassandra> configProvider;
    private Session session;
    private Cluster cluster;

    //This is to delay initialization till the lifecycle starts
    //Which means that configuration has already been loaded
    public LifeCycleManager(Provider<ConfigWithCassandra> configProvider, GuiceBundle.Builder guiceBuilder) {
        this.configProvider = configProvider;

        Provider<Session> sessionProvider = new Provider<Session>() {
            @Override
            public Session get() {
                return session;
            }
        };

        guiceBuilder.addModule(new CassandraModule(sessionProvider));
    }

    @Override
    public void start() throws Exception {
        Cluster.Builder builder = Cluster.builder();

        ConfigWithCassandra config = configProvider.get();

        for (String contactPoint : config.getCassandraConfig().getContactPoints()) {
            builder.addContactPoint(contactPoint);
        }

        if (config.getCassandraConfig().getUsername() != null
                && config.getCassandraConfig().getPassword() != null) {
            builder.withCredentials(config.getCassandraConfig().getUsername(), config.getCassandraConfig().getPassword());
        }

        cluster = builder.build();

        session = cluster.connect();
    }

    @Override
    public void stop() throws Exception {
        if (cluster != null) {
            cluster.close();
        }

        if (session != null) {
            session.close();
        }
    }
}
