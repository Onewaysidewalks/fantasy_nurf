package ninja.onewaysidewalks.riotapi.urf.matches.resources;


import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistence;

import javax.inject.Inject;

public class MatchesResourceImpl implements MatchesResource {

    private final MatchPersistence matchPersistence;

    @Inject
    public MatchesResourceImpl(MatchPersistence matchPersistence) {
        this.matchPersistence = matchPersistence;
    }

    @Override
    public Match getMatch(Long matchId) {
        return matchPersistence.getMatchById(matchId);
    }
}
