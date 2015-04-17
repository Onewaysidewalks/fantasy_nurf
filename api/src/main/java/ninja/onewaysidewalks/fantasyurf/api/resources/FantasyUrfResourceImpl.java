package ninja.onewaysidewalks.fantasyurf.api.resources;

import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchAcceptRequest;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchStartRequest;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchStartResponse;

import javax.validation.Valid;
import javax.ws.rs.PathParam;

public class FantasyUrfResourceImpl implements FantasyUrfResource {
    @Override
    public FantasyMatchStartResponse startMatch(@Valid FantasyMatchStartRequest startRequest) {
        return null;
    }

    @Override
    public FantasyMatch getMatch(@PathParam("match_id") String matchId) {
        return null;
    }

    @Override
    public void acceptMatch(@PathParam("match_id") String matchId, @Valid FantasyMatchAcceptRequest acceptRequest) {

    }
}
