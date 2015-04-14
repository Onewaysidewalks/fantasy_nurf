package ninja.onewaysidewalks.fantasyurf.stats.calculator.providers;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.ChampionSpecificStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.StatValue;
import ninja.onewaysidewalks.riotapi.models.Participant;

import java.math.BigDecimal;

public class KillsByChampionStatProvider implements ChampionSpecificStatProvider {

    @Override
    public StatValue getStatForChampionFromMatch(Participant participant) {
        StatValue statValue = new StatValue();
        statValue.setType(StatValue.StatType.CHAMPION_SPECIFIC);
        statValue.setValue(new BigDecimal(participant.getStats().getKills()));

        return statValue;
    }

    @Override
    public String getStatName() {
        return "kills";
    }
}
