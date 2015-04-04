package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

@Data
public class Player {
    private String matchHistoryUri;
    private String summonerName;
    private int profileIcon;
    private long summonerId;
}
