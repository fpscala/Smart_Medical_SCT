# --- !Ups
INSERT INTO "Work_type" ("work_type")
VALUES ('Oqituvchi');
INSERT INTO "Work_type" ("work_type")
VALUES ('Elektrik');

INSERT INTO "Organization" ("organization_name")
VALUES ('TATU');
INSERT INTO "Organization" ("organization_name")
VALUES ('UZBEKENERGIYA');

# --- !Downs
DELETE FROM "Work_type" WHERE work_type = 'Oqituvchi';
DELETE FROM "Work_type" WHERE work_type = 'Elektrik';
DELETE FROM "Organization" WHERE organization_name = 'TATU';
DELETE FROM "Organization" WHERE organization_name = 'UZBEKENERGIYA';
