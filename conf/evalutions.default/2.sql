# --- !Ups
ALTER TABLE "Organization"
    ADD COLUMN total_workers INT NULL;

ALTER TABLE "Patient" DROP COLUMN organization_id;

ALTER TABLE "Patient"
    ADD COLUMN organization_name VARCHAR NULL;

# --- !Downs
ALTER TABLE "Organization" DROP COLUMN total_workers;

ALTER TABLE "Patient" DROP COLUMN organization_name;

ALTER TABLE "Patient"
    ADD COLUMN organization_id INT NULL
        CONSTRAINT "organization_for_patient" REFERENCES "Organization" ("id") ON UPDATE CASCADE ON DELETE CASCADE;