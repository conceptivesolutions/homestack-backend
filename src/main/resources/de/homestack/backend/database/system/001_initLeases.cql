-- Create satelliteleases table
CREATE TABLE IF NOT EXISTS satelliteleases
(
    satelliteid UUID,
    leaseid     UUID,
    userid      UUID,
    PRIMARY KEY ( leaseid )
);