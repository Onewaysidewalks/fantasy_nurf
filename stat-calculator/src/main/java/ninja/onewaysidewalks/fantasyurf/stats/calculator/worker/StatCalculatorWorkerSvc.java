package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ninja.onewaysidewalks.cassandra.client.ConfigWithCassandra;
import ninja.onewaysidewalks.cassandra.client.LifeCycleManager;
import ninja.onewaysidewalks.fantasyurf.stats.persistence.StatPersistenceModule;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.supervisor.ProducerModule;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.worker.resources.ApiModule;
import ninja.onewaysidewalks.messaging.client.consumers.rabbitmq.guice.CompetingConsumerLifecycleManager;
import ninja.onewaysidewalks.riotapi.guice.RiotModule;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistenceModule;
import org.joda.time.DateTime;

import javax.inject.Provider;

/**
 * A simple service that prompts the creation of statistics, by pinging simple 'doWork' messages
 */
public class StatCalculatorWorkerSvc extends Application<StatCalculatorWorkerConfig> {

    private LifeCycleManager<StatCalculatorWorkerConfig> cassandraLifeCycleManager;
    private StatCalculatorWorkerConfig svcConfig;
    private Provider<Injector> injectorProvider;

    @Override
    public void initialize(Bootstrap<StatCalculatorWorkerConfig> bootstrap) {

        GuiceBundle.Builder<StatCalculatorWorkerConfig> builder = GuiceBundle.<StatCalculatorWorkerConfig>newBuilder()
                .enableAutoConfig(getClass().getPackage().getName());

        cassandraLifeCycleManager = new LifeCycleManager<>(new Provider<ConfigWithCassandra>() {
            @Override
            public ConfigWithCassandra get() {
                return svcConfig;
            }
        }, builder); //provides a lifecycle object, as well as registers a session into the guice container

        //Add modules for injection configuration
        builder = builder
                .addModule(new StatPersistenceModule())
                .addModule(new MatchPersistenceModule())
                .addModule(new RiotModule())
                .addModule(new ProducerModule())
                .addModule(new ApiModule());

        final GuiceBundle<StatCalculatorWorkerConfig> guiceBundle = builder.build();

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
    public void run(StatCalculatorWorkerConfig configuration, Environment environment) throws Exception {
        svcConfig = configuration; //initializes provider

        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //register cassandra
        environment.lifecycle().manage(cassandraLifeCycleManager);

        //register queue listener
        CompetingConsumerLifecycleManager<DateTime> competingConsumerLifecycleManager
                = new CompetingConsumerLifecycleManager<>(
                configuration.getMessagingConsumer(), injectorProvider.get(), StatCalculatorWorker.class);

        environment.lifecycle().manage(competingConsumerLifecycleManager);
    }

    public static void main(String[] args) throws Exception {
        new StatCalculatorWorkerSvc().run(args);
    }
}
