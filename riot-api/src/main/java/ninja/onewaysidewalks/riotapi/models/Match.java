package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.List;

@Data
public class Match {
    private int mapId;
    private long matchCreation, matchDuration, matchId;
    private String matchVersion;
    private String queueType;
    private String region;
    private String season;

    private String matchMode;
    private String matchType;
    private String platformId;

    private List<ParticipantIdentity> participantIdentities;
    private List<Participant> participants;
    private List<Team> teams;
    private Timeline timeline;
}
