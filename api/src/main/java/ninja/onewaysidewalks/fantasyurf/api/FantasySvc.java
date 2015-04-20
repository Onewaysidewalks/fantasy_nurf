package ninja.onewaysidewalks.fantasyurf.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.cassandra.client.LifeCycleManager;
import ninja.onewaysidewalks.fantasyurf.api.config.FantasySvcConfig;
import ninja.onewaysidewalks.fantasyurf.api.guice.ApiModule;
import ninja.onewaysidewalks.fantasyurf.api.persistence.FantasyPersistenceModule;
import ninja.onewaysidewalks.fantasyurf.stats.persistence.StatPersistenceModule;
import ninja.onewaysidewalks.riotapi.guice.RiotModule;

import javax.inject.Provider;

public class FantasySvc extends Application<FantasySvcConfig> {

    private FantasySvcConfig fantasySvcConfig;
    private LifeCycleManager<FantasySvcConfig> cassandraLifeCycleManager;

    /**
     * CommandLine Entry point for the api application
     * @param args command line arguments (i.e. server config.yml)
     */
    public static void main(String[] args) throws Exception {
        new FantasySvc().run(args);
    }

    @Override
    public void initialize(Bootstrap<FantasySvcConfig> bootstrap) {

        GuiceBundle.Builder<FantasySvcConfig> builder = GuiceBundle.<FantasySvcConfig>newBuilder()
                .enableAutoConfig(getClass().getPackage().getName())
                .addModule(new ApiModule())
                .addModule(new StatPersistenceModule())
                .addModule(new RiotModule())
                .addModule(new FantasyPersistenceModule())
                .setConfigClass(FantasySvcConfig.class);

        cassandraLifeCycleManager = new LifeCycleManager<>(new Provider<ConfigWithCassandra>() {
            @Override
            public ConfigWithCassandra get() {
                return fantasySvcConfig;
            }
        }, builder); //provides a lifecycle object, as well as registers a session into the guice container

        //Setup Guice Configuration
        GuiceBundle<FantasySvcConfig> guiceBundle = builder.build();

        bootstrap.addBundle(guiceBundle);

        super.initialize(bootstrap);
    }

    @Override
    public void run(FantasySvcConfig configuration, Environment environment) throws Exception {
        fantasySvcConfig = configuration; //initializes provider

        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //register cassandra
        environment.lifecycle().manage(cassandraLifeCycleManager);
    }
}
