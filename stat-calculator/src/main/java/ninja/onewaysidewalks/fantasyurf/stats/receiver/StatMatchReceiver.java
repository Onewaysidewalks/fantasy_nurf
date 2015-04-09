package ninja.onewaysidewalks.fantasyurf.stats.receiver;

import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;

@Slf4j
public class StatMatchReceiver implements MessageHandler<Long> {
    @Override
    public void handleMessage(Long message) {
        log.info("Match Written Event Received: {}", message);
    }

    @Override
    public Class<Long> getMessageType() {
        return Long.class;
    }
}
