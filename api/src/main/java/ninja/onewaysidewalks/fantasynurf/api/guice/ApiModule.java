package ninja.onewaysidewalks.fantasynurf.api.guice;

import com.google.inject.AbstractModule;
import ninja.onewaysidewalks.fantasynurf.api.resources.StatsResource;
import ninja.onewaysidewalks.fantasynurf.api.resources.StatsResourceImpl;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StatsResource.class).to(StatsResourceImpl.class);
    }
}
