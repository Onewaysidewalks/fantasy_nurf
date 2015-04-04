package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.List;

@Data
public class Participant {
    private int championId;
    private int participantId;
    private int spell1Id;
    private int spell2Id;
    private int teamId;

    private ParticipantStats stats;

    private ParticipantTimeline timeline;

    private List<Rune> runes;

    private List<Mastery> masteries;

    private String highestAchievedSeasonTier;
}
