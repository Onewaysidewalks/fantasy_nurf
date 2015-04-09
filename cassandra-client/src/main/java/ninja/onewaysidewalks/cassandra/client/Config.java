package ninja.onewaysidewalks.cassandra.client;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Config {
    private List<String> contactPoints = new ArrayList<>();
    private String username = null;
    private String password = null;
}
