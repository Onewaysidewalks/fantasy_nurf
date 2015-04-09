package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;

import static com.datastax.driver.core.querybuilder.QueryBuilder.add;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class MatchIdTimeBucketPersistenceImpl {

    private static final String STATS_KEYSPACE = "champion_stats";
    private static final String STATS_TIMEBUCKET_TABLE = "timebucket_match_ids";

    private final Session session;

    @Inject
    public MatchIdTimeBucketPersistenceImpl(Session session) {
        this.session = session;
    }


    public void saveMatchIdToTimeBucket(DateTime matchStartedTime, Long matchId, TimeBucketInterval timeBucketInterval) {

        DateTime timeBucket = getTimeBucket(matchStartedTime, timeBucketInterval);

        session.execute(QueryBuilder
                .update(STATS_KEYSPACE, STATS_TIMEBUCKET_TABLE)
                .with(add("match_ids", matchId))
                .where(eq("time_bucket", timeBucket.getMillis())));
    }

    public DateTime getTimeBucket(DateTime matchTime, TimeBucketInterval timeBucketInterval) {
        //TODO: handle other time intervals. Not needed for competition
        if (timeBucketInterval.equals(TimeBucketInterval.HOUR)) {
            return new DateTime(matchTime.getYear(), matchTime.getMonthOfYear(), matchTime.getDayOfMonth(), matchTime.getHourOfDay(), 0, DateTimeZone.UTC);
        } else {
            return matchTime;
        }
    }
}
