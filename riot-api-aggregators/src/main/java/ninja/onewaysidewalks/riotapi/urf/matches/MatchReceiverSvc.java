package ninja.onewaysidewalks.riotapi.urf.matches;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.cassandra.client.LifeCycleManager;
import ninja.onewaysidewalks.messaging.client.consumers.rabbitmq.guice.CompetingConsumerLifecycleManager;
import ninja.onewaysidewalks.riotapi.guice.RiotModule;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistenceModule;

import javax.inject.Provider;

public class MatchReceiverSvc extends Application<SvcConfig> {

    private LifeCycleManager<SvcConfig> cassandraLifeCycleManager;
    private SvcConfig svcConfig;
    private Provider<Injector> injectorProvider;

    @Override
    public void initialize(Bootstrap<SvcConfig> bootstrap) {

        GuiceBundle.Builder<SvcConfig> builder = GuiceBundle.<SvcConfig>newBuilder()
                .enableAutoConfig(getClass().getPackage().getName());

        cassandraLifeCycleManager = new LifeCycleManager<>(new Provider<ConfigWithCassandra>() {
            @Override
            public ConfigWithCassandra get() {
                return svcConfig;
            }
        }, builder); //provides a lifecycle object, as well as registers a session into the guice container

        //Add modules for injection configuration
        builder = builder
                .addModule(new MatchPersistenceModule())
                .addModule(new RiotModule())
                .addModule(new ApiModule());

        final GuiceBundle<SvcConfig> guiceBundle = builder.setConfigClass(SvcConfig.class).build();

        //Use this to provide the injector elsewhere when the injector is already setup
        injectorProvider = new Provider<Injector>() {
            @Override
            public Injector get() {
                return guiceBundle.getInjector();
            }
        };

        bootstrap.addBundle(guiceBundle);

        super.initialize(bootstrap);
    }

    @Override
    public void run(SvcConfig configuration, Environment environment) throws Exception {
        svcConfig = configuration; //initializes provider

        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //register cassandra
        environment.lifecycle().manage(cassandraLifeCycleManager);

        //register queue listener
        CompetingConsumerLifecycleManager<Long> competingConsumerLifecycleManager = new CompetingConsumerLifecycleManager<>(
                configuration.getMessagingConsumer(), injectorProvider.get(), MatchCompletedReceiver.class);

        environment.lifecycle().manage(competingConsumerLifecycleManager);
    }

    public static void main(String[] args) throws Exception {
        new MatchReceiverSvc().run(args);
    }
}
