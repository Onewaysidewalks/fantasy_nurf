package ninja.onewaysidewalks.cassandra.client.guice;

import com.datastax.driver.core.Session;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Provider;

public class CassandraModule extends AbstractModule {

    private final Provider<Session> sessionProvider;

    public CassandraModule(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    protected void configure() {

    }

    @Provides
    public Session getSession() {
        return sessionProvider.get();
    }
}
