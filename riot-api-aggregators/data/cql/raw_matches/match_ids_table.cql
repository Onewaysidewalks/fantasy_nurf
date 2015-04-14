CREATE TABLE IF NOT EXISTS match_ids (
    time_bucket bigint,
    match_ids list<bigint>,
    PRIMARY KEY(time_bucket)
);

//Note: This table is deprecated, and is used only as storage from the custom Riot Endpoint. For the match/timebucket storage
//That the statValue calculator will use, see the match_timebuckets table