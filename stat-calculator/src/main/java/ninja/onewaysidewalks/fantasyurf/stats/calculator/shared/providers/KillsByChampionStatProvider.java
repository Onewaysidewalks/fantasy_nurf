package ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.providers;

import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.ChampionSpecificStatProvider;
import ninja.onewaysidewalks.fantasyurf.stats.calculator.shared.StatValue;
import ninja.onewaysidewalks.riotapi.models.Participant;

public class KillsByChampionStatProvider implements ChampionSpecificStatProvider {

    @Override
    public StatValue getStatForChampionFromMatch(Participant participant) {
        StatValue statValue = new StatValue();
        statValue.setType(StatValue.StatType.CHAMPION_SPECIFIC);
        statValue.setValue(participant.getStats().getKills());

        return statValue;
    }

    @Override
    public String getStatName() {
        return "kills";
    }
}
