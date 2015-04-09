package ninja.onewaysidewalks.riotapi.urf.matches;

import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.riotapi.RiotClient;
import ninja.onewaysidewalks.riotapi.RiotClientImpl;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistence;

import javax.inject.Inject;

@Slf4j
public class MatchCompletedReceiver implements MessageHandler<Long> {

    private final MatchPersistence matchPersistence;
    private final RiotClient riotClient;

    @Inject
    public MatchCompletedReceiver(MatchPersistence matchPersistence, RiotClient riotClient) {
        this.matchPersistence = matchPersistence;
        this.riotClient = riotClient;
    }

    @Override
    public void handleMessage(Long matchId) {
        log.info("Received match id: {}", matchId);

        Match match = riotClient.getMatchById(RiotClientImpl.Region.NA, matchId); //TODO: make region come from configuration

        log.info("Pulled Match Details for match: {}", match);
        matchPersistence.saveMatch(matchId, match);
    }

    @Override
    public Class<Long> getMessageType() {
        return Long.class;
    }
}
