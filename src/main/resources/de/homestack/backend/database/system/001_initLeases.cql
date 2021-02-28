-- Create satelliteleases table
CREATE TABLE IF NOT EXISTS satelliteleases
(
    satelliteid UUID,
    leaseid     UUID,
    userid      TEXT,
    PRIMARY KEY ( leaseid )
);