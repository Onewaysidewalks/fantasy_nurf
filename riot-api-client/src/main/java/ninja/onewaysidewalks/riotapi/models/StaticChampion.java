package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

@Data
public class StaticChampion {
    private Integer id;
    private String name;
    private String title;
    private String key;
    private StaticChampionImage image;
}
