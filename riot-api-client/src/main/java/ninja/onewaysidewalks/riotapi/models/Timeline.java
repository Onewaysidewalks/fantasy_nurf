package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.List;

@Data
public class Timeline {
    private List<Frame> frames;
    private long frameInterval;
}
