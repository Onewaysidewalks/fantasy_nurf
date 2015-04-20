package ninja.onewaysidewalks.fantasyurf.api.resources.models;

import io.dropwizard.validation.MaxSize;
import lombok.Data;

import java.util.List;


@Data
public class Team {
    private String playerId; //for now since there is no security, this must be passed around directly

    private boolean isWinner;

    @MaxSize(2)
    private List<Integer> bans;

    private TeamPicks picks;
}