package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MatchPersistence.class).to(MatchPersistenceImpl.class);
    }
}
