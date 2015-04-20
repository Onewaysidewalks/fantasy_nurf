package ninja.onewaysidewalks.fantasyurf.api.persistence;

import com.google.inject.AbstractModule;

public class FantasyPersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FantasyMatchPersistence.class).to(FantasyMatchPersistenceImpl.class);
    }
}
