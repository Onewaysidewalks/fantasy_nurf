package ninja.onewaysidewalks.fantasynurf.api.resources;

import io.dropwizard.jersey.params.DateTimeParam;

import javax.ws.rs.*;

@Path("/stats")
public interface StatsResource {
    @Path("/{champion_id}")
    @GET
    @Produces("application/json")
    public Object getGameStats(@PathParam("champion_id") Integer championId,
                               @QueryParam("start_date") DateTimeParam startDate,
                               @QueryParam("end_date") DateTimeParam endDateParam);
}
