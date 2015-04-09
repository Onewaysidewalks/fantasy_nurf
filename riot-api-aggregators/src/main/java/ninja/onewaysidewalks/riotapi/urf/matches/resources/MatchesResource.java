package ninja.onewaysidewalks.riotapi.urf.matches.resources;

import ninja.onewaysidewalks.riotapi.models.Match;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/matches")
public interface MatchesResource {

    @Path("/{match_id}")
    @GET
    @Produces("application/json")
    Match getMatch(@PathParam("match_id") Long matchId);
}
