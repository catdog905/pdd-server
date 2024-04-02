CREATE SCHEMA pdd;

CREATE TABLE pdd.user
(
    gitlab_name VARCHAR NOT NULL PRIMARY KEY
);

CREATE TABLE pdd.user_replenishment
(
    ticket_id VARCHAR NOT NULL PRIMARY KEY,
    user_name VARCHAR NOT NULL REFERENCES pdd.user (gitlab_name),
    reward    INTEGER NOT NULL
);
