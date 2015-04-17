package ninja.onewaysidewalks.fantasyurf.api.resources;

import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchAcceptRequest;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchStartRequest;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchStartResponse;

import javax.validation.Valid;
import javax.ws.rs.*;

@Path("/fantasy_urf")
public interface FantasyUrfResource {

    @Path("/matches")
    @POST
    FantasyMatchStartResponse startMatch(@Valid FantasyMatchStartRequest startRequest);

    @Path("/matches/{match_id}")
    FantasyMatch getMatch(@PathParam("match_id") String matchId);

    @Path("/matches/{match_id}/accept")
    void acceptMatch(@PathParam("match_id") String matchId, @Valid FantasyMatchAcceptRequest acceptRequest);
}
