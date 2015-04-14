package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.joda.time.DateTime;

import java.util.Map;

import static com.datastax.driver.core.querybuilder.QueryBuilder.add;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class ChampionStatisticPersistenceImpl implements ChampionStatisticPersistence {

    private final Session session;
    private static final String STATS_KEYSPACE = "stats";
    private static final String HOUR_BUCKET_TABLE = "champion_hour_statistics";

    public ChampionStatisticPersistenceImpl(Session session) {
        this.session = session;
    }

    @Override
    public void saveStatisticsForTimeBucket(Map<Integer, Map<String, Double>> statistics, DateTime timeBucket) {
        for (Map.Entry<Integer, Map<String, Double>> statisticMapEntry : statistics.entrySet()) {
            String partitionKey = getPartitionKey(statisticMapEntry.getKey(), timeBucket);

//            for (Map.Entry<String, Double> stat : statisticMapEntry.getValue().entrySet()) {
//                session.execute(QueryBuilder.update(STATS_KEYSPACE, HOUR_BUCKET_TABLE)
//                        .with(add(stat.getKey(), stat.getValue()))
//                        .where(eq("champion_time_bucket", partitionKey)));
//            }

            session.execute(QueryBuilder.insertInto(STATS_KEYSPACE, HOUR_BUCKET_TABLE)
                    .value("stats", statisticMapEntry.getValue())
                    .value("champion_time_bucket", partitionKey));
        }
    }

    public String getPartitionKey(Integer championId, DateTime timeBucket) {
        return String.format("%s_%s", championId, timeBucket.getMillis());
    }
}
