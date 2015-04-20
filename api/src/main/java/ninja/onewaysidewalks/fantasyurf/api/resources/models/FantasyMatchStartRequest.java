package ninja.onewaysidewalks.fantasyurf.api.resources.models;

import lombok.Data;

@Data
public class FantasyMatchStartRequest {

    private String playerId; //Since there is no security, this has to be posted at the API directly, instead of coming from the authorization header

    private Team team;
}
