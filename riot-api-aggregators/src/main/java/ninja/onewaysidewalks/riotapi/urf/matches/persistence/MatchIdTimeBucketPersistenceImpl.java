package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.datastax.driver.core.querybuilder.QueryBuilder.add;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class MatchIdTimeBucketPersistenceImpl implements MatchIdTimeBucketPersistence {

    private static final String RAW_MATCHES_KEYSPACE = "raw_matches";
    private static final String MATCH_HOUR_TIMEBUCKETS_TABLE = "match_hour_timebuckets";

    private final Session session;

    @Inject
    public MatchIdTimeBucketPersistenceImpl(Session session) {
        this.session = session;
    }

    @Override
    public void saveMatchIdToTimeBucket(Long matchStartedTime, Long matchId, TimeBucketInterval timeBucketInterval) {

        DateTime timeBucket = getTimeBucket(matchStartedTime, timeBucketInterval);

        session.execute(QueryBuilder
                .update(RAW_MATCHES_KEYSPACE, MATCH_HOUR_TIMEBUCKETS_TABLE)
                .with(add("match_ids", matchId))
                .where(eq("time_bucket", timeBucket.getMillis())));
    }

    @Override
    public Set<Long> getMatchIdsByTime(DateTime time, TimeBucketInterval timeBucketInterval) {
        DateTime timeBucket = getTimeBucket(time.getMillis(), timeBucketInterval);

        ResultSet resultSet = session.execute(QueryBuilder
                .select().from(RAW_MATCHES_KEYSPACE, MATCH_HOUR_TIMEBUCKETS_TABLE)
                .where(eq("time_bucket", timeBucket.getMillis())));

        if (resultSet.isExhausted()) {
            return new HashSet<>();
        }

        Row row = resultSet.one();
        return row.getSet("match_ids", Long.class);
    }

    public DateTime getTimeBucket(Long matchTimeMills, TimeBucketInterval timeBucketInterval) {
        //TODO: handle other time intervals. Not needed for competition

        DateTime matchTime = new DateTime(matchTimeMills, DateTimeZone.UTC);

        if (timeBucketInterval.equals(TimeBucketInterval.HOUR)) {
            return new DateTime(matchTime.getYear(), matchTime.getMonthOfYear(), matchTime.getDayOfMonth(), matchTime.getHourOfDay(), 0, DateTimeZone.UTC);
        } else {
            return matchTime;
        }
    }
}
