package ninja.onewaysidewalks.riotapi.urf.matches;

import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.riotapi.RiotClient;
import ninja.onewaysidewalks.riotapi.RiotClientImpl;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchIdTimeBucketPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistence;
import ninja.onewaysidewalks.utilities.TimeBucketInterval;

import javax.inject.Inject;

@Slf4j
public class MatchCompletedReceiver implements MessageHandler<Long> {

    private final MatchPersistence matchPersistence;
    private final MatchIdTimeBucketPersistence matchIdTimeBucketPersistence;
    private final RiotClient riotClient;

    @Inject
    public MatchCompletedReceiver(
            MatchPersistence matchPersistence,
            MatchIdTimeBucketPersistence matchIdTimeBucketPersistence,
            RiotClient riotClient) {
        this.matchPersistence = matchPersistence;
        this.matchIdTimeBucketPersistence = matchIdTimeBucketPersistence;
        this.riotClient = riotClient;
    }

    @Override
    public void handleMessage(Long matchId) {
        log.info("Received match id: {}", matchId);

        //Check to see if it already exists
        Match match = matchPersistence.getMatchById(matchId);

        if (match == null) {
            //match not found, pull from riot API

            match = riotClient.getMatchById(RiotClientImpl.Region.NA, matchId); //TODO: make region come from configuration
            log.debug("Pulled Match Details for match: {}", match);
            matchPersistence.saveMatch(matchId, match);
        }

        //TODO: make this write to multiple time buckets for more stat granularities
        matchIdTimeBucketPersistence.saveMatchIdToTimeBucket(match.getMatchCreation(), matchId, TimeBucketInterval.HOUR);
    }

    @Override
    public Class<Long> getMessageType() {
        return Long.class;
    }
}
