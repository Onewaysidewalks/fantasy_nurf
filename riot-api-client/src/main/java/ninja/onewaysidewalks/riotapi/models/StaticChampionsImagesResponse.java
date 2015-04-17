package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

import java.util.Map;

@Data
public class StaticChampionsImagesResponse {
    private Map<String, StaticChampion> data;
}
