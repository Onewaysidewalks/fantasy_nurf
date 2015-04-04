CREATE TABLE IF NOT EXISTS match_ids (
    time_bucket bigint,
    match_ids list<bigint>,
    PRIMARY KEY(time_bucket)
);

