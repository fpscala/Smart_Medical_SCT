# --- !Ups
CREATE TABLE "Regions"(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "region" VARCHAR NOT NULL
);

CREATE TABLE "Towns"(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "town" VARCHAR NOT NULL,
    "region_id" INT NOT NULL
);

CREATE TABLE "Work_type"
(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "work_type" VARCHAR NOT NULL
);

CREATE TABLE "Doctor_type"
(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "doctor_type" VARCHAR NOT NULL
);

CREATE TABLE "Lab_type"
(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "lab_type" VARCHAR NOT NULL
);

CREATE TABLE "Organization"
(
    "id" SERIAL  NOT NULL PRIMARY KEY,
    "organization_name" VARCHAR NOT NULL,
    "address" VARCHAR NULL,
    "phone_number" VARCHAR NULL,
    "email" VARCHAR NULL,
    "workers_number" INTEGER NULL,
    total_workers INT NULL,
    "work_type" INT CONSTRAINT "OrganizationFkWork_typeId" REFERENCES "Work_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE "Checkup_period"(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "number_per_year"  INT NOT NULL,
    "work_type_id" INT CONSTRAINT "Checkup_periodFkWork_typeId" REFERENCES "Work_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "doctor_type_id" INT NULL CONSTRAINT "Checkup_periodFkDoctor_typeId" REFERENCES "Doctor_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "lab_type_id" INT NULL CONSTRAINT "Checkup_periodFkLab_typeId" REFERENCES "Lab_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "spec_part_json" jsonb NULL
);

CREATE TABLE "Patient"
(
    "id" SERIAL NOT NULL PRIMARY KEY,
    "first_name" VARCHAR NOT NULL,
    "middle_name" VARCHAR NOT NULL,
    "last_name" VARCHAR NOT NULL,
    "passport_sn" VARCHAR NULL,
    "gender" Int NOT NULL,
    "birthday" DATE NOT NULL,
    "region" VARCHAR NOT NULL,
    "city" VARCHAR NOT NULL,
    "address" VARCHAR NOT NULL,
    "phone_number" VARCHAR NULL,
    "card_number" VARCHAR NOT NULL,
    "work_type_id" INT
        CONSTRAINT "work_type_for_patient" REFERENCES "Work_type" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "last_checkup" TIMESTAMP NULL,
    "photo" VARCHAR   NULL,
    "organization_name" VARCHAR NULL
        CONSTRAINT "organization_for_patient" REFERENCES "Organization" ("organization_name") ON UPDATE CASCADE ON DELETE CASCADE
);


INSERT INTO "Regions" ("region")
VALUES
    ('Andijon viloyati'),
    ('Buxoro viloyati'),
    ('Farg`ona viloyati'),
    ('Jizzax viloyati'),
    ('Namangan viloyati'),
    ('Navoiy viloyati'),
    ('Qashqadaryo viloyati'),
    ('Samarqand viloyati'),
    ('Sirdaryo viloyati'),
    ('Surxondaryo viloyati'),
    ('Toshkent viloyati'),
    ('Xorazm viloyati'),
    ('Qoraqalpog`iston Respublikasi'),
    ('Toshkent shahri');

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Andijon shahri', 1),
    ('Xonabod shahri', 1),
    ('Andijon tumani', 1),
    ('Asaka tumani', 1),
    ('Baliqchi tumani', 1),
    ('Bo`z tumani', 1),
    ('Buloqboshi tumani', 1),
    ('Isboskan tumani', 1),
    ('Jalaquduq tumani', 1),
    ('Marxamat tumani', 1),
    ('Oltinko`l tumani', 1),
    ('Paxtaobod tumani', 1),
    ('Qo`rg`ontepa tumani', 1),
    ('Shahrixon tumani', 1),
    ('Ulug`nor tumani', 1),
    ('Xo`jaobod tumani', 1);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Buxoro shahri', 2),
    ('Kogon shahri', 2),
    ('Buxoro tumani', 2),
    ('G`ijduvon tumani', 2),
    ('Jondor tumani', 2),
    ('Kogon tumani', 2),
    ('Olot tumani', 2),
    ('Peshku tumani', 2),
    ('Qorovulbozor tumani', 2),
    ('Qorako`l tumani', 2),
    ('Romitan tumani', 2),
    ('Shofirkon tumani', 2),
    ('Vobkent tumani', 2);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Farg`ona shahri', 3),
    ('Marg`ilon shahri', 3),
    ('Quvasoy shahri', 3),
    ('Qo`qon shahri', 3),
    ('Beshariq tumani', 3),
    ('Bog`dod tumani', 3),
    ('Buvayda tumani', 3),
    ('Dang`ara tumani', 3),
    ('Farg`ona tumani', 3),
    ('Furqat tumani', 3),
    ('Oltiariq tumani', 3),
    ('O`zbekiston tumani', 3),
    ('Quva tumani', 3),
    ('Qo`shtepa tumani', 3),
    ('Rishton tumani', 3),
    ('So`x tumani', 3),
    ('Toshloq tumani', 3),
    ('Uchko`prik tumani', 3),
    ('Yozyovon tumani', 3);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Jizzax shahri', 4),
    ('Arnasoy tumani', 4),
    ('Baxmal tumani', 4),
    ('Do`stlik tumani', 4),
    ('Forish tumani', 4),
    ('G`allaorol tumani', 4),
    ('Mirzacho`l tumani', 4),
    ('Paxtakor tumani', 4),
    ('Sharof tumani', 4),
    ('Yangiobod tumani', 4),
    ('Zafarobod tumani', 4),
    ('Zarbdor tumani', 4),
    ('Zomin tumani', 4);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Namangan shahri', 5),
    ('Chortoq tumani', 5),
    ('Chust tumani', 5),
    ('Chust tumani', 5),
    ('Mingbuloq tumani', 5),
    ('Namangan tumani', 5),
    ('Norin tumani', 5),
    ('Pop tumani', 5),
    ('To`raqo`rg`on tumani', 5),
    ('Uychi tumani', 5),
    ('Uchqo`rg`on tumani', 5),
    ('Yangiqo`rg`on tumani', 5);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Navoiy shahri', 6),
    ('Karmana tumani', 6),
    ('Konimex tumani', 6),
    ('Navbahor tumani', 6),
    ('Nurota tumani', 6),
    ('Qiziltepa tumani', 6),
    ('Tomdi tumani', 6),
    ('Uchquduq tumani', 6),
    ('Xatirchi tumani', 6),
    ('Zarafshon tumani', 6);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Qarshi shahri', 7),
    ('Shaxrisabz shahri', 7),
    ('Chiroqchi tumani', 7),
    ('Dehqonobod tumani', 7),
    ('G`uzor tumani', 7),
    ('Kasbi tumani', 7),
    ('Kitob tumani', 7),
    ('Koson tumani', 7),
    ('Mirishkor tumani', 7),
    ('Muborak tumani', 7),
    ('Nishon tumani', 7),
    ('Qamashi tumani', 7),
    ('Qarshi tumani', 7),
    ('Shaxrisabz tumani', 7),
    ('Yakkabog` tumani', 7);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Samarqand shahri', 8),
    ('Kattaqo`rg`on shahri', 8),
    ('Bulung`ur tumani', 8),
    ('Ishtixon tumani', 8),
    ('Jomboy tumani', 8),
    ('Kattaqo`rg`on tumani', 8),
    ('Natpay tumani', 8),
    ('Nurobod tumani', 8),
    ('Oqdaryo tumani', 8),
    ('Payariq tumani', 8),
    ('Pastdarg`on tumani', 8),
    ('Paxtachi tumani', 8),
    ('Qo`shrabot tumani', 8),
    ('Samarqand tumani', 8),
    ('Toyloq tumani', 8),
    ('Urgut tumani', 8);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Guliston shahri', 9),
    ('Yangiyer shahri', 9),
    ('Shirin shahri', 9),
    ('Boyovut tumani', 9),
    ('Guliston tumani', 9),
    ('Mirzaobod tumani', 9),
    ('Oqoltin tumani', 9),
    ('Sardoba tumani', 9),
    ('Sayxunobod tumani', 9),
    ('Sirdaryo tumani', 9),
    ('Xovos tumani', 9);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Termiz shahri', 10),
    ('Angor tumani', 10),
    ('Boysun tumani', 10),
    ('Denov tumani', 10),
    ('Jarqo`rg`on tumani', 10),
    ('Muzrobod tumani', 10),
    ('Oltinsoy tumani', 10),
    ('Qiziriq tumani', 10),
    ('Qumqo`rg`on tumani', 10),
    ('Sariosiyo tumani', 10),
    ('Sherobod tumani', 10),
    ('Sho`rchi tumani', 10),
    ('Termiz tumani', 10),
    ('Uzun tumani', 10);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Angren shahri', 11),
    ('Bekobod shahri', 11),
    ('Chirchiq shahri', 11),
    ('Nurafshon shahri', 11),
    ('Olmaliq shahri', 11),
    ('Ohangaron shahri', 11),
    ('Yangiyo`l shahri', 11),
    ('Bekobod tumani', 11),
    ('Bo`ka tumani', 11),
    ('Bo`stonliq tumani', 11),
    ('Chinoz tumani', 11),
    ('Ohangaron tumani', 11),
    ('Oqqo`rg`on tumani', 11),
    ('O`rtachirchiq tumani', 11),
    ('Parkent tumani', 11),
    ('Piskent tumani', 11),
    ('Qibray tumani', 11),
    ('Quyichirchiq tumani', 11),
    ('Toshkent tumani', 11),
    ('Yangiyo`l tumani', 11),
    ('Yuqorichirchiq tumani', 11),
    ('Zangiota tumani', 11);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Urganch shahri', 12),
    ('Xiva shahri', 12),
    ('Bog`ot tumani', 12),
    ('Gurlan tumani', 12),
    ('Hazorasp tumani', 12),
    ('Qo`shko`pir tumani', 12),
    ('Shovot tumani', 12),
    ('Urganch tumani', 12),
    ('Xiva tumani', 12),
    ('Xonqa tumani', 12),
    ('Yangiariq tumani', 12),
    ('Yangibozor tumani', 12);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Nukus shahri', 13),
    ('Amudaryo tumani', 13),
    ('Beruniy tumani', 13),
    ('Chimboy tumani', 13),
    ('Ellikqala tumani', 13),
    ('Kegeyli tumani', 13),
    ('Mo`ynoq tumani', 13),
    ('Nukus tumani', 13),
    ('Qonliko`l tumani', 13),
    ('Qorao`zak tumani', 13),
    ('Qo`ng`irot tumani', 13),
    ('Taxiatosh tumani', 13),
    ('Taxtako`prik tumani', 13),
    ('To`rtko`l tumani', 13),
    ('Sho`manoy tumani', 13),
    ('Xo`jayli tumani', 13);

INSERT INTO "Towns" ("town", "region_id")
VALUES
    ('Bektemir tumani', 14),
    ('Chilonzor tumani', 14),
    ('Mirzo Ulug`bek tumani', 14),
    ('Mirobod tumani', 14),
    ('Olmazor tumani', 14),
    ('Sirgali tumani', 14),
    ('Shayxontohur tumani', 14),
    ('Uchtepa tumani', 14),
    ('Yakkasaroy tumani', 14),
    ('Yashnobod tumani', 14),
    ('Yunusobod tumani', 14);



# --- !Downs
DROP TABLE "Checkup_period";
DROP TABLE "Organization";
DROP TABLE "Work_type";
DROP TABLE "Doctor_type";
DROP TABLE "Lab_type";
DROP TABLE "Patient";
DROP TABLE "Regions";
DROP TABLE "Towns";

