CREATE SCHEMA pdd;

-- CREATE type pdd.role as ENUM ('DEV', 'ARC', 'REV');

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

-- CREATE TABLE pdd.project (
--     name VARCHAR PRIMARY KEY
-- );
--
-- CREATE TABLE pdd.task (
--     id UUID PRIMARY KEY,
--     role pdd.role NOT NULL,
--     budget INTEGER NOT NULL,
--     in_scope BOOLEAN NOT NULL,
--     is_solved BOOLEAN NOT NULL,
--     created_by VARCHAR NOT NULL,
--     created_date DATE NOT NULL,
--     assigned_user VARCHAR REFERENCES pdd.user(gitlab_name)
-- );
--
-- CREATE TABLE pdd.code_puzzle_definition(
--     task_id UUID REFERENCES pdd.task(id) PRIMARY KEY,
--     pdd_id VARCHAR NOT NULL
-- );
--
-- CREATE TABLE pdd.issue_puzzle_definition(
--     task_id UUID REFERENCES pdd.task(id) PRIMARY KEY,
--     issue_id INTEGER NOT NULL
-- );
--
-- CREATE TABLE pdd.blocked_task (
--     task_id UUID NOT NULL REFERENCES pdd.task(id)
-- );
--
-- CREATE TABLE pdd.task_banned_user (
--     task_id UUID NOT NULL REFERENCES pdd.task(id),
--     user_id VARCHAR NOT NULL REFERENCES pdd.user(gitlab_name),
--     reason VARCHAR NOT NULL
-- )
