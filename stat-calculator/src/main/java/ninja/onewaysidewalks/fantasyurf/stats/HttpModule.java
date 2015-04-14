package ninja.onewaysidewalks.fantasyurf.stats;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ninja.onewaysidewalks.fantasyurf.stats.supervisor.StatSupervisorConfig;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class HttpModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Client.class).toInstance(ClientBuilder.newClient());
    }

    @Provides
    @Named("matchApiUrl")
    public String getMatchApiUrl(StatSupervisorConfig config) {
        return config.getMatchApiUrl();
    }
}
