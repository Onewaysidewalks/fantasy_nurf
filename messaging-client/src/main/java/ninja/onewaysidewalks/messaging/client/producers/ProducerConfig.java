package ninja.onewaysidewalks.messaging.client.producers;

import lombok.Data;

@Data
public class ProducerConfig {
    private String host;
    private String username;
    private String password;
}
