package ninja.onewaysidewalks.fantasyurf.stats.calculator.worker.resources;

import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

@Data
public class CalculateRequest {
    @NotNull
    private DateTime timeBucket;
}
