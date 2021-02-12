-- Create stacks_by_id table
CREATE TABLE stacks_by_id
(
    id          UUID,
    displayName TEXT,
    PRIMARY KEY ( id )
);

-- Create devices_by_stackid table
CREATE TABLE devices_by_stackid
(
    stackid  UUID,
    id       UUID,
    icon     TEXT,
    address  TEXT,
    location TEXT,
    PRIMARY KEY ( stackid, id )
);

-- Create slots_by_deviceid table
CREATE TABLE slots_by_deviceid
(
    deviceid UUID,
    id       UUID,
    state    TEXT,
    targetid UUID,
    PRIMARY KEY ( deviceid, id )
);

-- Create metrics_by_deviceid table
CREATE TABLE metrics_by_deviceid
(
    deviceid UUID,
    id       UUID,
    type     TEXT,
    enabled  BOOLEAN,
    settings MAP<TEXT, TEXT>,
    PRIMARY KEY ( deviceid, id )
);

-- Create satellites_by_stackid table
CREATE TABLE satellites_by_stackid
(
    stackid UUID,
    id      UUID,
    PRIMARY KEY ( stackid, id )
);

-- Create satelliteleases_by_satelliteid table
CREATE TABLE satelliteleases_by_satelliteid
(
    satelliteid UUID,
    id          UUID,
    userid      UUID,
    revokeddate TIMESTAMP,
    accesstoken TEXT,
    PRIMARY KEY ( satelliteid, id )
);

-- Create records_by_metricid table
CREATE TABLE records_by_metricid
(
    metricid   UUID,
    id         UUID,
    recorddate TIMESTAMP,
    state      TEXT,
    result     MAP<TEXT, TEXT>,
    PRIMARY KEY ( metricid, recorddate )
);