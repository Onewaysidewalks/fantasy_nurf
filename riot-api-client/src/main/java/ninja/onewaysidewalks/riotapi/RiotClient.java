package ninja.onewaysidewalks.riotapi;

import ninja.onewaysidewalks.riotapi.models.Match;

import java.util.List;

public interface RiotClient {
    /**
     * This should only be used by the background service to pull match ids for the riot competition.
     * NO ONE else should use this
     * @param timestamp
     * @param region
     * @return
     */
    @Deprecated
    List<Long> getMatchIdsForUrf(long timestamp, RiotClientImpl.Region region);

    Match getMatchById(RiotClientImpl.Region region, long matchId);
}
