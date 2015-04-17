package ninja.onewaysidewalks.riotapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.riotapi.models.Champion;
import ninja.onewaysidewalks.riotapi.models.Match;
import ninja.onewaysidewalks.riotapi.models.StaticChampion;
import ninja.onewaysidewalks.riotapi.models.StaticChampionsImagesResponse;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RiotClientImpl implements RiotClient {
    public static String RIOT_API_KEY = System.getenv("RIOT_API_KEY");

    private final Client client;
    private final String apiBaseUrl;

    private static final String URF_URL = "v4.1/game/ids";

    private static final String MATCH_URL = "v2.2/match/%s";

    private static final String CHAMPIONS_IMAGE_URL = "%s/v1.2/champion"; //region included here

    private static final String DATA_DRAGON_CHAMPION_IMAGE_URL = "http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/%s"; //includes champion filename

    private final ObjectMapper objectMapper;

    public RiotClientImpl(ObjectMapper objectMapper) {
        if (RIOT_API_KEY == null) {
            throw new InvalidParameterException("Environment Variable RIOT_API_KEY must be set. (i.e. in /etc/environment)");
        }

        client = ClientBuilder.newClient();
        apiBaseUrl = "https://na.api.pvp.net/api/lol/%s/";
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Long> getMatchIdsForUrf(long timestamp, Region region) {

        String jsonString = getJsonResponseWithRateLimiting(
                client.target(String.format(apiBaseUrl, region.toString()) + URF_URL)
                .queryParam("api_key", RIOT_API_KEY)
                .queryParam("beginDate", timestamp)
                .request(MediaType.APPLICATION_JSON_TYPE));


        jsonString = jsonString.replace("[", "").replace("]", "");

        List<Long> retList = new ArrayList<>();

        if (jsonString.length() > 0) {
            for (String matchId : jsonString.split(",")) {
                retList.add(new Long(matchId));
            }
        }

        return retList;
    }

    public List<Champion> getChampions(Region region) {
        String jsonString = getJsonResponseWithRateLimiting(client
                .target(String.format(apiBaseUrl + CHAMPIONS_IMAGE_URL, "static-data", region))
                .queryParam("api_key", RIOT_API_KEY)
                .queryParam("champData", "image")
                .request(MediaType.APPLICATION_JSON_TYPE));

        if (StringUtils.isBlank(jsonString)) {
            return new ArrayList<>();
        }

        try {
            StaticChampionsImagesResponse response =
                    objectMapper.readValue(jsonString, StaticChampionsImagesResponse.class);

            List<Champion> champions = new ArrayList<>();

            for (Map.Entry<String, StaticChampion> staticChampionEntry : response.getData().entrySet()) {
                Champion champion = new Champion();

                champion.setId(staticChampionEntry.getValue().getId());
                champion.setImageUrl(String.format(DATA_DRAGON_CHAMPION_IMAGE_URL,
                        staticChampionEntry.getValue().getImage().getFull()));

                champions.add(champion);
            }

            return champions;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Match getMatchById(Region region, long matchId) {
        String jsonString = getJsonResponseWithRateLimiting(client.target(getMatchUrl(region, matchId))
                .queryParam("api_key", RIOT_API_KEY)
                .queryParam("includeTimeline", "true")
                .request(MediaType.APPLICATION_JSON_TYPE));

        if (StringUtils.isBlank(jsonString)) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, Match.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getMatchUrl(Region region, Long matchId) {
        return String.format(apiBaseUrl + MATCH_URL, region, matchId);
    }

    //TODO: make this more generic instead of taking the invocation builder
    public String getJsonResponseWithRateLimiting(Invocation.Builder requestBuilder) {
        String response;
        while (true) {
            try {
                response = requestBuilder.get(String.class);
                break;
            } catch (ClientErrorException ex) {
                if (ex.getResponse().getStatus() == 429) {
                    //Rate limited, wait 5 seconds and try again
                    log.warn("Rate Limited!");
                    try {
                        Thread.sleep(5000); //TODO: make this configurable
                    } catch (InterruptedException ie) {
                        throw new RuntimeException(ie);
                    }
                } else { //not rate limited, propogate up
                    throw ex;
                }
            }
        }

        return response;
    }

    public enum Region {
        NA;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
