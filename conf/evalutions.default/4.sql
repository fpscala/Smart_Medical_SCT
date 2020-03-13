# --- !Ups
ALTER TABLE "Checkup_period"
    ADD COLUMN spec_part_json jsonb NULL;

# --- !Downs
ALTER TABLE "Checkup_period" DROP COLUMN spec_part_json;
