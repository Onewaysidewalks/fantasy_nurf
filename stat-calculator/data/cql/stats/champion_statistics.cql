CREATE TABLE IF NOT EXISTS champion_statistics (
    champion_time_bucket text,
    stats map<text, decimal>,
    PRIMARY KEY(champion_time_bucket)
);

//The primary key is in the format of {championId}_{timebucket}. This is for somewhat easy loading, and safe persistence
//as the partitions will never get too large. Similarly, the data is evenly distributed by both champion and timebucket,
//so there will be some mitigation to hotspots for both read and writes.