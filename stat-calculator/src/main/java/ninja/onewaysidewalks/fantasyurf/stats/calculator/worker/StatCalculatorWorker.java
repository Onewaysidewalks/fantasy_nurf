package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker;

import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.StatsBuilder;
import ninja.onewaysidewalks.fantasyurf.stats.persistence.ChampionStatisticPersistence;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchIdTimeBucketPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistence;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.utilities.TimeBucketInterval;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

@Slf4j
public class StatCalculatorWorker implements MessageHandler<DateTime> {

    private final MatchPersistence matchPersistence;
    private final MatchIdTimeBucketPersistence matchIdTimeBucketPersistence;
    private final ChampionStatisticPersistence championStatisticPersistence;

    @Inject
    public StatCalculatorWorker(
            MatchPersistence matchPersistence,
            MatchIdTimeBucketPersistence matchIdTimeBucketPersistence,
            ChampionStatisticPersistence championStatisticPersistence) {
        this.matchPersistence = matchPersistence;
        this.matchIdTimeBucketPersistence = matchIdTimeBucketPersistence;
        this.championStatisticPersistence = championStatisticPersistence;
    }

    @Override
    public void handleMessage(DateTime timeBucketMessage) {
        log.info("Calculating stats for timebucket: {}", timeBucketMessage);

        Set<Long> matchIds = matchIdTimeBucketPersistence.getMatchIdsByTime(timeBucketMessage, TimeBucketInterval.HOUR);

        if (matchIds.size() == 0) {
            return;
        }

        StatsBuilder statsBuilder = StatsBuilder.start();
        for (Long matchId : matchIds) {
            Match match = matchPersistence.getMatchById(matchId);

            if (match != null) {
                statsBuilder.withMatch(match);
            }
        }

        Map<Integer, Map<String, Double>> statisticsMap = statsBuilder.buildStats();

        championStatisticPersistence.saveStatisticsForTimeBucket(statisticsMap, timeBucketMessage);
    }

    @Override
    public Class<DateTime> getMessageType() {
        return DateTime.class;
    }
}
