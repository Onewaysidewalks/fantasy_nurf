package ninja.onewaysidewalks.riotapi;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class RiotClientImpl implements RiotClient {
    public static String RIOT_API_KEY = System.getenv("RIOT_API_KEY");

    private final Client client;
    private final String apiBaseUrl;

    private static final String URF_URL = "v4.1/game/ids";

    public RiotClientImpl() {
        if (RIOT_API_KEY == null) {
            throw new InvalidParameterException("Environment Variable RIOT_API_KEY must be set. (i.e. in /etc/environment)");
        }

        client = ClientBuilder.newClient();
        apiBaseUrl = "https://na.api.pvp.net/api/lol/%s/";
    }

    @Override
    public List<Long> getMatchIdsForUrf(long timestamp, Region region) {

        String jsonString = client.target(String.format(apiBaseUrl, region.toString()) + URF_URL)
                .queryParam("api_key", RIOT_API_KEY)
                .queryParam("beginDate", timestamp)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        jsonString = jsonString.replace("[", "").replace("]", "");

        List<Long> retList = new ArrayList<>();

        if (jsonString.length() > 0) {
            for (String matchId : jsonString.split(",")) {
                retList.add(new Long(matchId));
            }
        }

        return retList;
    }

    public enum Region {
        NA;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
