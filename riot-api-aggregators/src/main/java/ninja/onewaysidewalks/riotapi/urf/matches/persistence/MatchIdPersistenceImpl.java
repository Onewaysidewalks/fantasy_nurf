package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

@Slf4j
public class MatchIdPersistenceImpl implements ninja.onewaysidewalks.riotapi.urf.matches.persistence.MatchIdPersistence {

    private Cluster cluster;
    private Session session;
    private String keyspace;

    public MatchIdPersistenceImpl(String keyspace, String contactPoint, String username, String password) {
        cluster = Cluster.builder()
                .withCredentials(username, password)
                .addContactPoint(contactPoint).build();
        this.keyspace = keyspace;
    }

    public void initialize() {
        session = cluster.connect(keyspace);
    }

    public void tearDown() {
        session.close();
        cluster.close();
    }

    @Override
    public void writeBucket(long timestamp, List<Long> matchIds) {
        session.execute(QueryBuilder
                .insertInto("match_ids")
                .value("time_bucket", timestamp)
                .value("match_ids", matchIds));
    }

    @Override
    public boolean bucketExists(long timestamp) {
        Row row = session.execute(QueryBuilder.select().from("match_ids").where(eq("time_bucket", timestamp))).one();

        if (row == null) {
            return false;
        }

        if (!row.getColumnDefinitions().contains("match_ids")) {
            return false;
        }

        List<Long> matchIds = row.getList("match_ids", Long.class);
        if (matchIds == null
                || matchIds.size() == 0) {
            log.warn("Record written for time with no match ids");
            return  false;
        } else {
            return true;
        }
    }
}
