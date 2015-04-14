package ninja.onewaysidewalks.fantasyurf.stats.calculator;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.providers.*;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.models.Participant;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

public class StatsBuilder {

    /**
     * Map of championId, and stats
     */
    private final Map<Integer, Map<String, DescriptiveStatistics>> statisticsMap = new HashMap<>();
    private ChampionSpecificStatProvider[] championSpecificStatProviders;

    public StatsBuilder(ChampionSpecificStatProvider[] championSpecificStatProviders) {
        this.championSpecificStatProviders = championSpecificStatProviders;
    }

    public StatsBuilder withMatch(Match match) {

        for (Participant participant : match.getParticipants()) {
            for (ChampionSpecificStatProvider statProvider : championSpecificStatProviders) {
                StatValue statValue = statProvider.getStatForChampionFromMatch(participant);

                //This champion hasnt been seen yet, initialize his/her/its mapping
                if (!statisticsMap.containsKey(participant.getChampionId())) {
                    statisticsMap.put(participant.getChampionId(), new HashMap<String, DescriptiveStatistics>());
                }

                //This champion does not yet have this particular statValue recorded, initlialize its statValue holder (i.e. DescriptiveStatistics)
                if (!statisticsMap.get(participant.getChampionId()).containsKey(statProvider.getStatName())) {
                    statisticsMap.get(participant.getChampionId()).put(statProvider.getStatName(), new DescriptiveStatistics());
                }

                statisticsMap.get(participant.getChampionId()).get(statProvider.getStatName()).addValue(statValue.getValue());
            }
        }

        return this;
    }

    /**
     * Used to take a builder into a well formatted stat list
     * @return a map of champion id, to map of {statname, statvalue} for said champion
     */
    public Map<Integer, Map<String, Double>> buildStats() {
        if (statisticsMap.size() == 0) {
            return new HashMap<>();
        }

        Map<Integer, Map<String, Double>> stats = new HashMap<>();

        for (Map.Entry<Integer, Map<String, DescriptiveStatistics>> statisticsMapEntry : statisticsMap.entrySet()) {
            int championId = statisticsMapEntry.getKey();

            if (statisticsMapEntry.getValue().size() == 0) {
                continue; //no stats calculated for champion, skip.
            }

            if (!stats.containsKey(statisticsMapEntry.getKey())) {
                stats.put(championId, new HashMap<String, Double>());
            }

            for (Map.Entry<String, DescriptiveStatistics> statisticsEntry : statisticsMapEntry.getValue().entrySet()) {

                String statName = statisticsEntry.getKey();

                //add to stats here
                double mean = statisticsEntry.getValue().getMean();
                double max = statisticsEntry.getValue().getMax();
                double min = statisticsEntry.getValue().getMin();
                double fiftiethPercentile = statisticsEntry.getValue().getPercentile(50);
                double seventyFifthPercentile = statisticsEntry.getValue().getPercentile(75);
                double ninetiethPercentile = statisticsEntry.getValue().getPercentile(90);
                double ninetyNinthPercentile = statisticsEntry.getValue().getPercentile(99);

                stats.get(championId).put(StatNames.mean(statName), mean);
                stats.get(championId).put(StatNames.max(statName), max);
                stats.get(championId).put(StatNames.min(statName), min);
                stats.get(championId).put(StatNames.p50(statName), fiftiethPercentile);
                stats.get(championId).put(StatNames.p75(statName), seventyFifthPercentile);
                stats.get(championId).put(StatNames.p90(statName), ninetiethPercentile);
                stats.get(championId).put(StatNames.p99(statName), ninetyNinthPercentile);
            }
        }

        return stats;
    }

    public static StatsBuilder start() {
        return new StatsBuilder(new ChampionSpecificStatProvider[] {
                new KillsByChampionStatProvider(),
                new GoldEarnedByChampionStatProvider(),
                new GoldSpentByChampionStatProvider(),
                new AssistsByChampionStatProvider(),
                new DeathsByChampionStatProvider()
        });
    }

    public static StatsBuilder start(ChampionSpecificStatProvider[] statProviders) {
        return new StatsBuilder(statProviders);
    }
}
