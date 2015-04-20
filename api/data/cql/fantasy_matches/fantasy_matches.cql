CREATE TABLE IF NOT EXISTS fantasy_matches (
    id text,
    player_id text,
    state text,
    winner_id text,
    team_json text,
    PRIMARY KEY(id, player_id)
);


//A wide row that will consist of three rows:
//One row for the first player starting the match
//One row for the player who accepts the match
//One row for match level details (who is winner, state, etc.)
