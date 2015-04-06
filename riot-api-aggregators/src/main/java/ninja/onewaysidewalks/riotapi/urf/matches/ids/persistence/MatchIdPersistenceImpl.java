package ninja.onewaysidewalks.riotapi.urf.matches.ids.persistence;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

@Slf4j
public class MatchIdPersistenceImpl implements MatchIdPersistence {

    private final Session session;
    private static final String MATCH_ID_TABLE = "match_ids";
    private static final String RAW_MATCHES_KEYSPACE = "raw_matches";

    public MatchIdPersistenceImpl(Session session) {
        this.session = session;
    }
    @Override
    public void writeBucket(long timestamp, List<Long> matchIds) {
        session.execute(QueryBuilder
                .insertInto(RAW_MATCHES_KEYSPACE, MATCH_ID_TABLE)
                .value("time_bucket", timestamp)
                .value("match_ids", matchIds));
    }

    @Override
    public boolean bucketExists(long timestamp) {
        Row row = session.execute(QueryBuilder.select().from(RAW_MATCHES_KEYSPACE, MATCH_ID_TABLE)
                .where(eq("time_bucket", timestamp))).one();

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
