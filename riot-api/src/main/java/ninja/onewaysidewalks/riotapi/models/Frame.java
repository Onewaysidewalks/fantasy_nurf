package ninja.onewaysidewalks.riotapi.models;

import java.util.List;
import java.util.Map;

public class Frame {
    private List<Event> events;
    private Map<String, ParticipantFrame> participantFrames;
    private long timestamp;
}
