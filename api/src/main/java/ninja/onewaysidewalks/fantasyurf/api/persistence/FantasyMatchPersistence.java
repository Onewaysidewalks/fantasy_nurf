package ninja.onewaysidewalks.fantasyurf.api.persistence;

import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;

import java.util.List;

public interface FantasyMatchPersistence {
    void startMatch(FantasyMatch fantasyMatch);
    FantasyMatch getMatchById(String matchId);
    List<FantasyMatch> getMatchesByPlayerId(String playerId);
}
