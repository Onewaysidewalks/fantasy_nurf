package ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.providers;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.ChampionSpecificStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.StatValue;
import ninja.onewaysidewalks.riotapi.models.Participant;

public class GoldSpentByChampionStatProvider implements ChampionSpecificStatProvider {
    @Override
    public StatValue getStatForChampionFromMatch(Participant participant) {
        return new StatValue(participant.getStats().getGoldSpent(), StatValue.StatType.CHAMPION_SPECIFIC);
    }

    @Override
    public String getStatName() {
        return "gold-spent";
    }
}