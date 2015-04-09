package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

@Data
public class ParticipantFrame {
    private int currentGold;
    private int jungleMinionsKilled;
    private int level;
    private int minionsKilled;
    private int participantId;
    private int totalGold;
    private int xp;
    private Position position;
}
