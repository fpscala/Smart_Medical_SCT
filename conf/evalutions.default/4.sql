# --- !Ups
CREATE TABLE "Tmp_table"
(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "work_type_id" INT CONSTRAINT "Tmp_tableFkWork_typeId" REFERENCES "Work_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "doctor_type_id" INT CONSTRAINT "Tmp_tableFkDoctor_typeId" REFERENCES "Doctor_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "lab_type_id" INT CONSTRAINT "Tmp_tableFkLab_typeId" REFERENCES "Lab_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE "Checkup_period" DROP COLUMN doctor_type_id;
ALTER TABLE "Checkup_period" DROP COLUMN lab_type_id;
ALTER TABLE "Checkup_period" DROP COLUMN work_type_id;
ALTER TABLE "Checkup_period" ADD COLUMN "tmp_table_id" INT CONSTRAINT "Checkup_periodFkTmp_tableId" REFERENCES "Tmp_table" ("id") ON UPDATE CASCADE ON DELETE CASCADE;

# --- !Downs
DROP TABLE "Tmp_table";
ALTER TABLE "Checkup_period" ADD COLUMN doctor_type_id JSONB;
ALTER TABLE "Checkup_period" ADD COLUMN lab_type_id JSONB;
ALTER TABLE "Checkup_period" ADD COLUMN work_type_id INT CONSTRAINT "work_type" REFERENCES "Work_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "Checkup_period" DROP COLUMN tmp_table_id;
