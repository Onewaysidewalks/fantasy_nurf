package ninja.onewaysidewalks.riotapi.urf.matches.ids.persistence;

import java.util.List;

public interface MatchIdPersistence {
    void writeBucket(long timestamp, List<Long> matchIds);
    boolean bucketExists(long timestamp);
}
