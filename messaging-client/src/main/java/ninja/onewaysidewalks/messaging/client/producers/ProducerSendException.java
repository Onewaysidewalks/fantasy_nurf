package ninja.onewaysidewalks.messaging.client.producers;

public class ProducerSendException extends RuntimeException {
    public ProducerSendException(Exception ex) {
        super("Producer exception while sending:",ex);
    }
}
