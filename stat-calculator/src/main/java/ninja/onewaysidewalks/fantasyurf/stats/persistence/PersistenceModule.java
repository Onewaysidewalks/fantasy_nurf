package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import com.google.inject.AbstractModule;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchIdTimeBucketPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchIdTimeBucketPersistenceImpl;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistenceImpl;

public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MatchIdTimeBucketPersistence.class).to(MatchIdTimeBucketPersistenceImpl.class);
        bind(MatchPersistence.class).to(MatchPersistenceImpl.class);
    }
}
