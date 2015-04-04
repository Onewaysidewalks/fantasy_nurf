package ninja.onewaysidewalks.riotapi;

import java.util.List;

public interface RiotClient {
    List<Long> getMatchIdsForUrf(long timestamp, RiotClientImpl.Region region);
}
