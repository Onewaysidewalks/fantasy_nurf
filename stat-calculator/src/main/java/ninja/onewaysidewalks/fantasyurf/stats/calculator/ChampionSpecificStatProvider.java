package ninja.onewaysidewalks.fantasyurf.stats.calculator;

import ninja.onewaysidewalks.riotapi.models.Participant;

public interface ChampionSpecificStatProvider {
    StatValue getStatForChampionFromMatch(Participant participant);

    String getStatName();
}
