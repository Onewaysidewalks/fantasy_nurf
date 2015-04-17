package ninja.onewaysidewalks.fantasyurf.api.resources.models;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.*;

@Data
public class ChampionStatistics {
    private DateTime startTime;
    private DateTime endTime;

    private Map<String, List<StatEntry>> stats = new HashMap<>();
    private List<String> availableStats = new ArrayList<>();
}
