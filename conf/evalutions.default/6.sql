# --- !Ups
ALTER TABLE "Patient"
    ADD COLUMN region INT NULL
        CONSTRAINT "PatientFkRegionId" REFERENCES "Regions" ("id") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE "Patient"
    ADD COLUMN city INT NULL
        CONSTRAINT "PatientFkTownId" REFERENCES "Towns" ("id") ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE "Patient" DROP COLUMN profession;

# --- !Downs
ALTER TABLE "Patient" DROP COLUMN region;

ALTER TABLE "Patient" DROP COLUMN city;

ALTER TABLE "Patient"
    ADD COLUMN profession VARCHAR NULL;