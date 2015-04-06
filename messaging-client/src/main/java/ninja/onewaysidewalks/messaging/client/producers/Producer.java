package ninja.onewaysidewalks.messaging.client.producers;

public interface Producer {
    <T> void sendMessage(String topic, T message);
}
