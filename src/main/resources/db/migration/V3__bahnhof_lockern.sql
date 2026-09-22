ALTER TABLE bahnhof
    DROP CONSTRAINT ck_bahnhof_typ;

ALTER TABLE bahnhof
    DROP CONSTRAINT uq_bahnhof_rang;

ALTER TABLE bahnhof
    ALTER COLUMN typ DROP NOT NULL;

ALTER TABLE bahnhof
    ALTER COLUMN rang DROP NOT NULL;
