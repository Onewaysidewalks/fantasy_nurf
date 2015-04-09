package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Frame {
    private List<Event> events;
    private Map<String, ParticipantFrame> participantFrames;
    private long timestamp;
}
