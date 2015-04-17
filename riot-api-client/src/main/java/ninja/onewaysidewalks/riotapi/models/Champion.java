package ninja.onewaysidewalks.riotapi.models;

import lombok.Data;

@Data
public class Champion {
    private Integer id;
    private String name;
    private String title;
    private String imageUrl; //This will be built based on ddragon
}
