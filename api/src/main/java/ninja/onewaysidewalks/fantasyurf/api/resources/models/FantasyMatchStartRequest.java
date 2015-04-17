package ninja.onewaysidewalks.fantasyurf.api.resources.models;

import lombok.Data;

@Data
public class FantasyMatchStartRequest {
    private Team team;

    private String invitee; //for now, since there is no security, this must match the other players id
}
