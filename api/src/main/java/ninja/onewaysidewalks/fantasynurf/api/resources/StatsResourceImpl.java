package ninja.onewaysidewalks.fantasynurf.api.resources;

import io.dropwizard.jersey.params.DateTimeParam;

import javax.inject.Inject;

public class StatsResourceImpl implements StatsResource {
    @Inject
    public StatsResourceImpl() {

    }

    @Override
    public Object getGameStats(
            Integer championId,
            DateTimeParam startDate,
            DateTimeParam endDateParam) {



        return null;
    }
}
