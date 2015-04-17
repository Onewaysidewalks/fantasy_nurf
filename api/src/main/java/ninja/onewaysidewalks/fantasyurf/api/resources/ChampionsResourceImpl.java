package ninja.onewaysidewalks.fantasyurf.api.resources;

import io.dropwizard.jersey.params.DateTimeParam;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.Champion;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.ChampionStatistics;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.StatEntry;
import ninja.onewaysidewalks.fantasyurf.stats.persistence.ChampionStatistic;
import ninja.onewaysidewalks.fantasyurf.stats.persistence.ChampionStatisticPersistence;
import ninja.onewaysidewalks.riotapi.RiotClient;
import ninja.onewaysidewalks.riotapi.RiotClientImpl;

import javax.inject.Inject;
import java.util.*;

public class ChampionsResourceImpl implements ChampionsResource {

    private final ChampionStatisticPersistence championStatisticPersistence;
    private final RiotClient riotClient;

    @Inject
    public ChampionsResourceImpl(
            ChampionStatisticPersistence championStatisticPersistence,
            RiotClient riotClient) {
        this.championStatisticPersistence = championStatisticPersistence;
        this.riotClient = riotClient;
    }

    @Override
    public ChampionStatistics getStats(
            Integer championId,
            DateTimeParam startDateParam,
            DateTimeParam endDate) {

        List<ChampionStatistic> statistics = championStatisticPersistence.getChampionStats(
                championId, startDateParam.get(), endDate.get());

        if (statistics.size() == 0) {
            return null; //404
        }

        ChampionStatistics championStatistics = new ChampionStatistics();
        championStatistics.setEndTime(endDate.get());
        championStatistics.setStartTime(startDateParam.get());

        Set<String> availableStats = new HashSet<>();
        for (ChampionStatistic stat : statistics) {
            for (Map.Entry<String, Double> statValue : stat.getStats().entrySet()) {

                if (!availableStats.contains(statValue.getKey())) {
                    availableStats.add(statValue.getKey());
                }

                StatEntry statEntry = new StatEntry();
                statEntry.setTime(stat.getTime());
                statEntry.setValue(statValue.getValue());

                if (!championStatistics.getStats().containsKey(statValue.getKey())) {
                    championStatistics.getStats().put(statValue.getKey(), new ArrayList<StatEntry>());
                }

                championStatistics.getStats().get(statValue.getKey()).add(statEntry);
            }
        }

        List<String> sortedStats = new ArrayList<>(availableStats);
        Collections.sort(sortedStats);

        championStatistics.setAvailableStats(sortedStats);

        return championStatistics;
    }

    @Override
    public List<Champion> getChampions() {

        List<ninja.onewaysidewalks.riotapi.models.Champion> riotChampions
                = riotClient.getChampions(RiotClientImpl.Region.NA);

        List<Champion> champions = new ArrayList<>();

        for (ninja.onewaysidewalks.riotapi.models.Champion riotChamp : riotChampions) {
            Champion champion = new Champion();
            champion.setId(riotChamp.getId());
            champion.setImageUrl(riotChamp.getImageUrl());

            champions.add(champion);
        }

        return champions;
    }
}
