package ninja.onewaysidewalks.riotapi.urf.matches.shared;

import lombok.Data;

@Data
public class MatchRecordedMessage {
    private Long matchId;
    private Long matchCreationMillis;
}
