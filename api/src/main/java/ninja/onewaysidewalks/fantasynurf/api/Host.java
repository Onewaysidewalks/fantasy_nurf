package ninja.onewaysidewalks.fantasynurf.api;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ninja.onewaysidewalks.fantasynurf.api.config.HostConfiguration;
import ninja.onewaysidewalks.fantasynurf.api.guice.ApiModule;

public class Host extends Application<HostConfiguration> {

    /**
     * CommandLine Entry point for the api application
     * @param args command line arguments (i.e. server config.yml)
     */
    public static void main(String[] args) throws Exception {
        new Host().run(args);
    }

    @Override
    public void initialize(Bootstrap<HostConfiguration> bootstrap) {

        //Setup Guice Configuration
        GuiceBundle<HostConfiguration> guiceBundle = GuiceBundle.<HostConfiguration>newBuilder()
                .enableAutoConfig(getClass().getPackage().getName())
                .addModule(new ApiModule())
                .setConfigClass(HostConfiguration.class).build();


        bootstrap.addBundle(guiceBundle);

        super.initialize(bootstrap);
    }

    @Override
    public void run(HostConfiguration configuration, Environment environment) throws Exception {

    }
}
