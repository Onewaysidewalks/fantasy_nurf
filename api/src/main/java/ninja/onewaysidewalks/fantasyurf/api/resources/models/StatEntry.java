package ninja.onewaysidewalks.fantasyurf.api.resources.models;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class StatEntry {
    private Double value;
    private DateTime time;
}
