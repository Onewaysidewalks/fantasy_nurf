package ninja.onewaysidewalks.fantasyurf.api.resources;

import io.dropwizard.jersey.params.DateTimeParam;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.Champion;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.ChampionStatistics;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.util.List;

@Path("/champions")
public interface ChampionsResource {
    @Path("/{champion_id}/stats")
    @GET
    @Produces("application/json")
    ChampionStatistics getStats(@PathParam("champion_id") Integer championId,
                           @QueryParam("start_date") @NotNull DateTimeParam startDate,
                           @QueryParam("end_date") @NotNull DateTimeParam endDate);

    @Path("/")
    @GET
    @Produces("application/json")
    List<Champion> getChampions();
}
