CREATE TABLE zug (
    zug_nummer VARCHAR(255) PRIMARY KEY,
    zug_typ    VARCHAR(255)
);

CREATE TABLE fahrten (
    fahrt_id            BIGSERIAL PRIMARY KEY,
    zug_nummer          VARCHAR(255) NOT NULL,
    start_bahnhof      VARCHAR(255),
    ziel_bahnhof       VARCHAR(255),
    abfahrtszeit_plan   TIMESTAMP,
    ankunftszeit_plan   TIMESTAMP,
    abfahrtszeit_ist    TIMESTAMP,
    ankunftszeit_ist    TIMESTAMP,
    "timestamp"        TIMESTAMP,
    CONSTRAINT fk_fahrt_zug FOREIGN KEY (zug_nummer) REFERENCES zug (zug_nummer)
);

CREATE INDEX idx_fahrten_zug_nummer ON fahrten (zug_nummer);
