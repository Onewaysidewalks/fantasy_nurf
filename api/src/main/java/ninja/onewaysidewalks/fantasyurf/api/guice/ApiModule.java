package ninja.onewaysidewalks.fantasyurf.api.guice;

import com.google.inject.AbstractModule;
import ninja.onewaysidewalks.fantasyurf.api.resources.ChampionsResource;
import ninja.onewaysidewalks.fantasyurf.api.resources.ChampionsResourceImpl;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ChampionsResource.class).to(ChampionsResourceImpl.class);
    }
}
