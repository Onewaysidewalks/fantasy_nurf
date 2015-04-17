package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

public interface ChampionStatisticPersistence {
    void saveStatisticsForTimeBucket(Map<Integer, Map<String, Double>> statistics, DateTime timeBucket);

    List<ChampionStatistic> getChampionStats(Integer championId, DateTime startTime, DateTime endTime);
}
