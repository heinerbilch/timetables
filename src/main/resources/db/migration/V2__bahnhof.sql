CREATE TABLE bahnhof (
    eva_nummer VARCHAR(16) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    typ        VARCHAR(16)  NOT NULL,
    rang       INT          NOT NULL
);

ALTER TABLE bahnhof
    ADD CONSTRAINT uq_bahnhof_rang UNIQUE (rang);

ALTER TABLE bahnhof
    ADD CONSTRAINT ck_bahnhof_typ CHECK (typ IN ('HBF', 'SBAHN'));
