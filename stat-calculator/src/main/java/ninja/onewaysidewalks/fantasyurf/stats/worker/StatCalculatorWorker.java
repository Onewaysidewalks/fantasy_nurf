package ninja.onewaysidewalks.fantasyurf.stats.worker;

import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.StatsBuilder;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchIdTimeBucketPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchPersistence;
import ninja.onewaysidewalks.riotapi.urf.matches.persistence.TimeBucketInterval;
import ninja.onewaysidewalks.messaging.client.consumers.MessageHandler;
import ninja.onewaysidewalks.riotapi.urf.matches.shared.MatchRecordedMessage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class StatCalculatorWorker implements MessageHandler<DateTime> {

    private final MatchPersistence matchPersistence;
    private final MatchIdTimeBucketPersistence matchIdTimeBucketPersistence;

    @Inject
    public StatCalculatorWorker(
            MatchPersistence matchPersistence,
            MatchIdTimeBucketPersistence matchIdTimeBucketPersistence) {
        this.matchPersistence = matchPersistence;
        this.matchIdTimeBucketPersistence = matchIdTimeBucketPersistence;
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

            statsBuilder.withMatch(match);
        }

        Map<Integer, Map<String, Double>> statisticsMap = statsBuilder.buildStats();

        //TODO: save map to database
    }

    @Override
    public Class<DateTime> getMessageType() {
        return DateTime.class;
    }
}
