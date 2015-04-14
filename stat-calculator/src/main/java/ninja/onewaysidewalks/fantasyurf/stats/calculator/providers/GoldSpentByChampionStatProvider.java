package ninja.onewaysidewalks.fantasyurf.stats.calculator.providers;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.ChampionSpecificStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.StatValue;
import ninja.onewaysidewalks.riotapi.models.Participant;

import java.math.BigDecimal;

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