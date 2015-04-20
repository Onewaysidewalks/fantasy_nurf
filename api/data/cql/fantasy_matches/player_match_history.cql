CREATE TABLE IF NOT EXISTS fantasy_match_history (
    player_id text,
    match_ids list<text>,
    PRIMARY KEY(player_id)
);