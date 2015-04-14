package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import org.joda.time.DateTime;

import java.util.Map;

public interface ChampionStatisticPersistence {
    void saveStatisticsForTimeBucket(Map<Integer, Map<String, Double>> statistics, DateTime timeBucket);
}
