# --- !Ups
ALTER TABLE "Patient"
    ADD COLUMN spec_part_json jsonb NULL;

# --- !Downs
ALTER TABLE "Patient" DROP COLUMN spec_part_json;