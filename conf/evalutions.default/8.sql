# --- !Ups

CREATE TABLE "Role"
(
    "id"          SERIAL  NOT NULL PRIMARY KEY,
    "name"        VARCHAR NOT NULL,
    "description" VARCHAR NOT NULL,
    "code"        VARCHAR NOT NULL UNIQUE
);

CREATE TABLE "User"
(
    "id"           SERIAL  NOT NULL PRIMARY KEY,
    "first_name"   VARCHAR NOT NULL,
    "last_name"    VARCHAR NOT NULL,
    "login"        VARCHAR NOT NULL UNIQUE,
    "phone"        VARCHAR NOT NULL,
    "password"     VARCHAR NOT NULL,
    "email"        VARCHAR NULL,
    "photo"        VARCHAR NULL,
    "created_at" DATE    NOT NULL,
    "disabled"     BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE "Permission"
(
    "id"      SERIAL NOT NULL PRIMARY KEY,
    "role_id" INT
        CONSTRAINT PermissionFkRoleId REFERENCES "Role" ("id") ON UPDATE CASCADE ON DELETE CASCADE,
    "user_id" INT
        CONSTRAINT PermissionFkUserId REFERENCES "User" ("id") ON UPDATE CASCADE ON DELETE CASCADE
);

# --- !Downs

DROP TABLE "Permission";
DROP TABLE "User";
DROP TABLE "Role";