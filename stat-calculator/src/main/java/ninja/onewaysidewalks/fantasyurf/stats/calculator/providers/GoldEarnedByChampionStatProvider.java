package ninja.onewaysidewalks.fantasyurf.stats.calculator.providers;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.ChampionSpecificStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.StatValue;
import ninja.onewaysidewalks.riotapi.models.Participant;

import java.math.BigDecimal;

public class GoldEarnedByChampionStatProvider implements ChampionSpecificStatProvider {
    @Override
    public StatValue getStatForChampionFromMatch(Participant participant) {
        return new StatValue(new BigDecimal(participant.getStats().getGoldEarned()), StatValue.StatType.CHAMPION_SPECIFIC);
    }

    @Override
    public String getStatName() {
        return "gold-earned";
    }
}
