package ninja.onewaysidewalks.messaging.client.consumers;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompetingConsumerConfig extends ConsumerConfig {
    private String queue;
}
