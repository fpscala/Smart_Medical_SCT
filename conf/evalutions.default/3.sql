# --- !Ups
ALTER TABLE public."Organization"
    ADD COLUMN address varchar;

ALTER TABLE public."Organization"
    ADD COLUMN phone_number varchar;

ALTER TABLE public."Organization"
    ADD COLUMN email varchar;

ALTER TABLE public."Organization"
    ADD COLUMN workers_number integer;

ALTER TABLE public."Organization"
    ADD COLUMN work_type jsonb;

# --- !Downs
ALTER TABLE public."Organization" DROP COLUMN address;

ALTER TABLE public."Organization" DROP COLUMN phone_number;

ALTER TABLE public."Organization" DROP COLUMN email;

ALTER TABLE public."Organization" DROP COLUMN workers_number;

ALTER TABLE public."Organization" DROP COLUMN work_type;
