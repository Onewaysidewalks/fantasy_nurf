package ninja.onewaysidewalks.riotapi.urf.matches.persistence;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.riotapi.models.Match;

import java.io.IOException;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

@Slf4j
public class MatchPersistenceImpl implements MatchPersistence {

    private final Session session;
    private final ObjectMapper objectMapper;

    private static final String MATCHES_TABLE = "matches";
    private static final String RAW_MATCHES_KEYSPACE = "raw_matches";

    public MatchPersistenceImpl(ObjectMapper objectMapper, Session session) {
        this.session = session;
        this.objectMapper = objectMapper;
    }


    @Override
    public Match getMatchById(Long id) {
        Row row = session.execute(QueryBuilder.select().from(RAW_MATCHES_KEYSPACE, MATCHES_TABLE)
                .where(eq("match_id", id))).one();

        if (row == null) {
            return null;
        }

        String json = row.getString("match_json");

        try {
            return objectMapper.readValue(json, Match.class);
        } catch (IOException ex) {
            log.error("Could not deserialize match json {}", json, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveRawMatch(Long matchId, String matchJson) {
        session.execute(QueryBuilder
                .insertInto(RAW_MATCHES_KEYSPACE, MATCHES_TABLE)
                .value("match_id", matchId)
                .value("match_json", matchJson)
                .setConsistencyLevel(ConsistencyLevel.ONE));
    }
}
