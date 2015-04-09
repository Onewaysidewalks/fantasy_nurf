package ninja.onewaysidewalks.riotapi.urf.matches;

import com.google.inject.AbstractModule;
import ninja.onewaysidewalks.riotapi.urf.matches.resources.MatchesResource;
import ninja.onewaysidewalks.riotapi.urf.matches.resources.MatchesResourceImpl;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MatchesResource.class).to(MatchesResourceImpl.class);
    }
}
