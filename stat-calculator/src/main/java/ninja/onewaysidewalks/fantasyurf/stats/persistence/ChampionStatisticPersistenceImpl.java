package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import ninja.onewaysidewalks.utilities.TimeBucketHelper;
import ninja.onewaysidewalks.utilities.TimeBucketInterval;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class ChampionStatisticPersistenceImpl implements ChampionStatisticPersistence {

    private final Session session;
    private static final String STATS_KEYSPACE = "stats";
    private static final String HOUR_BUCKET_TABLE = "champion_hour_statistics";

    @Inject
    public ChampionStatisticPersistenceImpl(Session session) {
        this.session = session;
    }

    @Override
    public void saveStatisticsForTimeBucket(Map<Integer, Map<String, Double>> statistics, DateTime timeBucket) {
        for (Map.Entry<Integer, Map<String, Double>> statisticMapEntry : statistics.entrySet()) {
            String partitionKey = getPartitionKey(statisticMapEntry.getKey(), timeBucket);

            session.execute(QueryBuilder.insertInto(STATS_KEYSPACE, HOUR_BUCKET_TABLE)
                    .value("stats", statisticMapEntry.getValue())
                    .value("champion_time_bucket", partitionKey));
        }
    }

    @Override
    public List<ChampionStatistic> getChampionStats(Integer championId, DateTime startTime, DateTime endTime) {

        List<DateTime> timeBuckets = TimeBucketHelper.getTimeBuckets(startTime, endTime, TimeBucketInterval.HOUR);
        List<ChampionStatistic> championStatistics = new ArrayList<>();

        for (DateTime bucket : timeBuckets) {
            ResultSet resultSet = session.execute(QueryBuilder.select().from(STATS_KEYSPACE, HOUR_BUCKET_TABLE)
                .where(eq("champion_time_bucket", getPartitionKey(championId, bucket))));

            Row row = resultSet.one();

            if (row == null) {
                continue;
            }

            ChampionStatistic statistic = new ChampionStatistic();
            statistic.setTime(bucket);
            statistic.setChampionId(championId);
            statistic.setStats(row.getMap("stats", String.class, Double.class));

            championStatistics.add(statistic);
        }

        return championStatistics;
    }

    public String getPartitionKey(Integer championId, DateTime timeBucket) {
        return String.format("%s_%s", championId, timeBucket.getMillis());
    }
}
