package ninja.onewaysidewalks.fantasyurf.stats.calculator.shared;

import ninja.onewaysidewalks.riotapi.models.Participant;

public interface ChampionSpecificStatProvider {
    StatValue getStatForChampionFromMatch(Participant participant);

    String getStatName();
}
