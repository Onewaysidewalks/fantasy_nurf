package ninja.onewaysidewalks.riotapi.urf.matches;

import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;

@Slf4j
public class MatchCompletedReceiver implements MessageHandler<Long> {

    public MatchCompletedReceiver() {

    }

    @Override
    public void handleMessage(Long matchId) {
        log.debug("Received match id: {}", matchId);
    }

    @Override
    public Class<Long> getMessageType() {
        return Long.class;
    }
}
