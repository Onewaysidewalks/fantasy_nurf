package ninja.onewaysidewalks.fantasyurf.api.persistence;

import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.Team;

import java.util.List;

public interface FantasyMatchPersistence {
    void startMatch(String matchId, String playerId, Team team);
    FantasyMatch getMatchById(String matchId);
    List<FantasyMatch> getMatchesByPlayerId(String playerId);
    void acceptMatch(String matchId, String playerId, Team team);
}
