package ninja.onewaysidewalks.fantasyurf.stats.calculator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatValue {
    private double value;
    private StatType type;

    public enum StatType {
        CHAMPION_SPECIFIC, //Only in regards to a certain champion
        CHAMPION_VERSUS_CHAMPION
    }
}
