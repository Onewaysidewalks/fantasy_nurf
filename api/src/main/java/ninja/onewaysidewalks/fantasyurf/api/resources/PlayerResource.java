package ninja.onewaysidewalks.fantasyurf.api.resources;

import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/players")
public interface PlayerResource {
    @Path("/{player_id}/match_history")
    @GET
    @Produces("application/json")
    List<FantasyMatch> getMatchHistory(@PathParam("player_id") String playerId);
}
