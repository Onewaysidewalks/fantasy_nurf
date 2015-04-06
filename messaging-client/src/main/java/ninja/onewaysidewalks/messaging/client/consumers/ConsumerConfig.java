package ninja.onewaysidewalks.messaging.client.consumers;

import lombok.Data;

@Data
public abstract class ConsumerConfig {
    private String host;
    private String username;
    private String password;
    private String topic;

    //TODO: add fields such as when to timeout, concurrent consumers, etc.
}
