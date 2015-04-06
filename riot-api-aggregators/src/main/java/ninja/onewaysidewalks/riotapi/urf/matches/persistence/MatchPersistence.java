package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import ninja.onewaysidewalks.riotapi.models.Match;

public interface MatchPersistence {
    Match getMatchById(Long id);
    void saveRawMatch(Long id, String matchJson);
}
