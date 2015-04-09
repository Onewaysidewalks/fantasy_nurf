package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.List;

@Data
public class Event {
    private int creatorId;
    private int itemAfter;
    private int itemBefore;
    private int itemId;
    private int killerId;
    private int participantId;
    private int skillSlot;
    private int teamId;
    private int victimId;
    private String buildingType;
    private String eventType;
    private String laneType;
    private String levelUpType;
    private String monsterType;
    private String towerType;
    private String wardType;
    private List<Integer> assistingParticipantIds;
    private Position position;
    private long timestamp;
}
