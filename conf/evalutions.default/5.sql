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




# --- !Downs
DROP TABLE "Regions";
DROP TABLE "Towns";

DELETE FROM "Regions" WHERE region = 'Andijon viloyati';
DELETE FROM "Regions" WHERE region = 'Buxoro viloyati';
DELETE FROM "Regions" WHERE region = 'Farg`ona viloyati';
DELETE FROM "Regions" WHERE region = 'Jizzax viloyati';
DELETE FROM "Regions" WHERE region = 'Namangan viloyati';
DELETE FROM "Regions" WHERE region = 'Navoiy viloyati';
DELETE FROM "Regions" WHERE region = 'Qashqadaryo viloyati';
DELETE FROM "Regions" WHERE region = 'Samarqand viloyati';
DELETE FROM "Regions" WHERE region = 'Sirdaryo viloyati';
DELETE FROM "Regions" WHERE region = 'Surxondaryo viloyati';
DELETE FROM "Regions" WHERE region = 'Toshkent viloyati';
DELETE FROM "Regions" WHERE region = 'Xorazm viloyati';
DELETE FROM "Regions" WHERE region = 'Qoraqalpog`iston Respublikasi';
DELETE FROM "Regions" WHERE region = 'Toshkent shahri';