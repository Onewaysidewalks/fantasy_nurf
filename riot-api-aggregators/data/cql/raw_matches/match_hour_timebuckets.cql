//For usage in statValue calculations

CREATE TABLE IF NOT EXISTS match_hour_timebuckets (
    time_bucket timestamp,
    match_ids set<bigint>,
    PRIMARY KEY(time_bucket)
);

