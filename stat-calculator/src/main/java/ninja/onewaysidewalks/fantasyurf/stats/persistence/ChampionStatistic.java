package ninja.onewaysidewalks.fantasyurf.stats.persistence;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

@Data
public class ChampionStatistic {
    private Map<String, Double> stats = new HashMap<>();

    private DateTime time;

    private Integer championId;
}
