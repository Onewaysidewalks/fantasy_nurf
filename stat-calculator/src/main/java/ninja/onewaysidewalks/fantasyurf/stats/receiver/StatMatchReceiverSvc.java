package ninja.onewaysidewalks.fantasyurf.stats.receiver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.cassandra.client.LifeCycleManager;
import ninja.onewaysidewalks.fantasyurf.stats.persistence.PersistenceModule;
import ninja.onewaysidewalks.messaging.client.consumers.rabbitmq.guice.CompetingConsumerLifecycleManager;
import ninja.onewaysidewalks.riotapi.guice.RiotModule;

import javax.inject.Provider;

public class StatMatchReceiverSvc extends Application<StatMatchReceiverConfig> {

    private LifeCycleManager<StatMatchReceiverConfig> cassandraLifeCycleManager;
    private StatMatchReceiverConfig svcConfig;
    private Provider<Injector> injectorProvider;

    @Override
    public void initialize(Bootstrap<StatMatchReceiverConfig> bootstrap) {

        GuiceBundle.Builder<StatMatchReceiverConfig> builder = GuiceBundle.newBuilder();

        cassandraLifeCycleManager = new LifeCycleManager<>(new Provider<ConfigWithCassandra>() {
            @Override
            public ConfigWithCassandra get() {
                return svcConfig;
            }
        }, builder); //provides a lifecycle object, as well as registers a session into the guice container

        //Add modules for injection configuration
        builder = builder
                .addModule(new PersistenceModule())
                .addModule(new RiotModule());

        final GuiceBundle<StatMatchReceiverConfig> guiceBundle = builder.build();

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
    public void run(StatMatchReceiverConfig configuration, Environment environment) throws Exception {
        svcConfig = configuration; //initializes provider

        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //register cassandra
        environment.lifecycle().manage(cassandraLifeCycleManager);

        //register queue listener
        CompetingConsumerLifecycleManager<Long> competingConsumerLifecycleManager = new CompetingConsumerLifecycleManager<>(
                configuration.getMessagingConsumer(), injectorProvider.get(), StatMatchReceiver.class);

        environment.lifecycle().manage(competingConsumerLifecycleManager);
    }

    public static void main(String[] args) throws Exception {
        new StatMatchReceiverSvc().run(args);
    }
}
