package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.List;

@Data
public class Team {
    private List<Ban> bans;

    private int baronKills;
    private int dragonKills;
    private int inhibitorKills;
    private int teamId;
    private int towerKills;
    private int vilemawKills;

    private boolean firstBaron;
    private boolean firstBlood;
    private boolean firstDragon;
    private boolean firstInhibitor;
    private boolean firstTower;

    private boolean winner;

    private long dominionVictoryScore;
}
