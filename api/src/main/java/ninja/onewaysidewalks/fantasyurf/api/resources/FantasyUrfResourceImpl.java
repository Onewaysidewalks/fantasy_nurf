package ninja.onewaysidewalks.fantasyurf.api.resources;

import ninja.onewaysidewalks.fantasyurf.api.persistence.FantasyMatchPersistence;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchAcceptRequest;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchStartRequest;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatchStartResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.PathParam;
import java.util.UUID;

public class FantasyUrfResourceImpl implements FantasyUrfResource {

    private final FantasyMatchPersistence fantasyMatchPersistence;

    @Inject
    public FantasyUrfResourceImpl(FantasyMatchPersistence fantasyMatchPersistence) {
        this.fantasyMatchPersistence = fantasyMatchPersistence;
    }

    @Override
    public FantasyMatchStartResponse startMatch(@Valid FantasyMatchStartRequest startRequest) {

        String matchId = UUID.randomUUID().toString();

        fantasyMatchPersistence.startMatch(matchId, startRequest.getPlayerId(), startRequest.getTeam());

        FantasyMatchStartResponse response = new FantasyMatchStartResponse();
        response.setMatchId(matchId);

        return response;
    }

    @Override
    public FantasyMatch getMatch(String matchId) {
        return fantasyMatchPersistence.getMatchById(matchId);
    }

    @Override
    public void acceptMatch(String matchId, @Valid FantasyMatchAcceptRequest acceptRequest) {
        fantasyMatchPersistence.acceptMatch(matchId, acceptRequest.getPlayerId(), acceptRequest.getTeam());
    }
}
