package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import java.util.List;

public interface MatchIdPersistence {
    void writeBucket(long timestamp, List<Long> matchIds);
    boolean bucketExists(long timestamp);
    void initialize();
    void tearDown();
}
