package ninja.onewaysidewalks.messaging.client.consumers;

import java.io.IOException;

public interface Consumer<T> {
    void start() throws IOException;
    void stop() throws IOException;
}
