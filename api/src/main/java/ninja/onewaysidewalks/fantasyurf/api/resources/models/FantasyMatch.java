package ninja.onewaysidewalks.fantasyurf.api.resources.models;

import io.dropwizard.validation.MaxSize;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FantasyMatch {
    private String id;

    private Team home;
    private Team away;

    private FantasyMatchState state;

    public enum FantasyMatchState {
        INVITED,
        IN_PROGRESS,
        DONE
    }
}

@Data
class Team {
    private String playerId; //for now since there is no security, this must be passed around directly

    private boolean isWinner;

    @MaxSize(2)
    private List<Integer> bans;

    private TeamPicks picks;
}

@Data
class TeamPicks {
    @MaxSize(2)
    private List<ChampionPick> first;

    @MaxSize(2)
    private List<ChampionPick> second;

    @MaxSize(2)
    private List<ChampionPick> third;

    @MaxSize(2)
    private List<ChampionPick> fourth;

    @MaxSize(2)
    private List<ChampionPick> fifth;
}

@Data
class ChampionPick {
    @NotNull
    private Integer id;

    @NotNull
    private ChampionPickRole role;

    //TODO: have better role support? These currently match what current meta is.
    public enum ChampionPickRole {
        JUNGLE,
        MID,
        SOLO,
        SUPPORT,
        ADC
    }
}