package ninja.onewaysidewalks.fantasyurf.api.persistence;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.FantasyMatch;
import ninja.onewaysidewalks.fantasyurf.api.resources.models.Team;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.add;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.set;

@Slf4j
public class FantasyMatchPersistenceImpl implements FantasyMatchPersistence {

    private final Session session;
    private final ObjectMapper objectMapper;

    private static final String FANTASY_KEYSPACE = "fantasy";
    private static final String FANTASY_MATCHES_TABLE = "fantasy_matches";
    private static final String FANTASY_MATCH_HISTORY_TABLE = "fantasy_match_history";

    private static final String MATCH_DETAILS_ID = "match-level-details";

    @Inject
    public FantasyMatchPersistenceImpl(Session session, ObjectMapper objectMapper) {
        this.session = session;
        this.objectMapper = objectMapper;
    }

    @Override
    public void startMatch(String matchId, String playerId, Team team) {
        //This executes three requests
        //One to start the match for a specific player
        //One to be match level details for that match
        //And one to players match history for lookup later
        try {
            session.execute(QueryBuilder
                    .batch(
                            QueryBuilder.insertInto(FANTASY_KEYSPACE, FANTASY_MATCHES_TABLE) //Player Entry
                                    .value("match_id", matchId)
                                    .value("player_id", playerId)
                                    .value("team_json", objectMapper.writeValueAsString(team)),
                            QueryBuilder.insertInto(FANTASY_KEYSPACE, FANTASY_MATCHES_TABLE) //Match Detail Entry
                                    .value("match_id", matchId)
                                    .value("player_id", MATCH_DETAILS_ID)
                                    .value("state", FantasyMatch.FantasyMatchState.INVITED)
                                    .value("winner_id", "NONE"),
                            QueryBuilder.update(FANTASY_KEYSPACE, FANTASY_MATCH_HISTORY_TABLE) //Match History Entry for player
                                    .with(add("match_ids", matchId))
                                    .where(eq("player_id", playerId))
                    ));
        } catch (IOException ex) {
            log.error("unable to execute batch", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public FantasyMatch getMatchById(String matchId) {
        return null;
    }

    @Override
    public List<FantasyMatch> getMatchesByPlayerId(String playerId) {
        return null;
    }

    @Override
    public void acceptMatch(String matchId, String playerId, Team team) {
        //This executes three requests
        //One to mark the match  in progess and associate the team
        //One to be match level details for that match
        //And one to players match history for lookup later
        try {
            session.execute(QueryBuilder
                    .batch(
                            QueryBuilder.insertInto(FANTASY_KEYSPACE, FANTASY_MATCHES_TABLE) //Player Entry
                                    .value("match_id", matchId)
                                    .value("player_id", playerId)
                                    .value("team_json", objectMapper.writeValueAsString(team)),
                            QueryBuilder.update(FANTASY_KEYSPACE, FANTASY_MATCHES_TABLE) //Match Detail Entry
                                    .with(set("state", FantasyMatch.FantasyMatchState.IN_PROGRESS.toString()))
                                    .where(eq("match_id", matchId)),
                            QueryBuilder.update(FANTASY_KEYSPACE, FANTASY_MATCH_HISTORY_TABLE) //Match History Entry for player
                                    .with(add("match_ids", matchId))
                                    .where(eq("player_id", playerId))
                    ));
        } catch (IOException ex) {
            log.error("unable to execute batch", ex);
            throw new RuntimeException(ex);
        }
    }
}
