-- Create stacks_by_id table
CREATE TABLE IF NOT EXISTS stacks_by_id
(
    id          UUID,
    displayName TEXT,
    PRIMARY KEY ( id )
);

-- Create devices_by_stackid table
CREATE TABLE IF NOT EXISTS devices_by_stackid
(
    stackid  UUID,
    id       UUID,
    icon     TEXT,
    address  TEXT,
    location TEXT,
    slots    TEXT,
    PRIMARY KEY ( stackid, id )
);

-- Create metrics_by_deviceid table
CREATE TABLE IF NOT EXISTS metrics_by_deviceid
(
    deviceid UUID,
    id       UUID,
    type     TEXT,
    enabled  BOOLEAN,
    settings MAP<TEXT, TEXT>,
    PRIMARY KEY ( deviceid, id )
);

-- Create satellites_by_stackid table
CREATE TABLE IF NOT EXISTS satellites_by_stackid
(
    stackid UUID,
    id      UUID,
    PRIMARY KEY ( stackid, id )
);

-- Create satelliteleases_by_satelliteid table
CREATE TABLE IF NOT EXISTS satelliteleases_by_satelliteid
(
    satelliteid UUID,
    id          UUID,
    userid      TEXT,
    revokeddate TIMESTAMP,
    accesstoken TEXT,
    PRIMARY KEY ( satelliteid, id )
);

-- Create records_by_metricid table
CREATE TABLE IF NOT EXISTS records_by_metricid
(
    metricid   UUID,
    id         UUID,
    recorddate TIMESTAMP,
    state      TEXT,
    result     MAP<TEXT, TEXT>,
    PRIMARY KEY ( metricid, recorddate )
);