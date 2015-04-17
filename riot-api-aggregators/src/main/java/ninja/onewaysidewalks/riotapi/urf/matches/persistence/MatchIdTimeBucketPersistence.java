package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import ninja.onewaysidewalks.utilities.TimeBucketInterval;
import org.joda.time.DateTime;

import java.util.Set;

public interface MatchIdTimeBucketPersistence {
    void saveMatchIdToTimeBucket(Long matchCreationMillis, Long matchId, TimeBucketInterval timeBucketInterval);
    Set<Long> getMatchIdsByTime(DateTime time, TimeBucketInterval timeBucketInterval);
}
