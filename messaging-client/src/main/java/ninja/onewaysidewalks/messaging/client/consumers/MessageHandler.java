package ninja.onewaysidewalks.messaging.client.consumers;

public interface MessageHandler<T> {
    void handleMessage(T message);
    Class<T> getMessageType();
}
