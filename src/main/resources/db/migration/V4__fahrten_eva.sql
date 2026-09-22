ALTER TABLE fahrten
    ADD COLUMN eva_nummer VARCHAR(16);

ALTER TABLE fahrten
    ADD CONSTRAINT fk_fahrt_bahnhof FOREIGN KEY (eva_nummer)
        REFERENCES bahnhof (eva_nummer);

CREATE INDEX idx_fahrten_eva_nummer ON fahrten (eva_nummer);
