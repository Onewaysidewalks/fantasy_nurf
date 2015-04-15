package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker.resources;

import com.google.inject.AbstractModule;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AdminResource.class).to(AdminResourceImpl.class);
    }
}
