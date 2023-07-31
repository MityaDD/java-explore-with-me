DROP TABLE IF EXISTS comments, requests, event_compilations, events, compilations, categories, users CASCADE ;

create table if not exists users (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR(250) NOT NULL,
email VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
annotation VARCHAR(2000) NOT NULL,
category_id BIGINT REFERENCES categories(id) ON DELETE RESTRICT,
confirmed_requests INTEGER,
created_on TIMESTAMP NOT NULL,
description VARCHAR(7000) NOT NULL,
event_date TIMESTAMP NOT NULL,
initiator_id BIGINT REFERENCES users(id),
lat FLOAT NOT NULL,
lon FLOAT NOT NULL,
paid BOOLEAN NOT NULL,
participant_limit INTEGER,
published_on TIMESTAMP,
request_moderation BOOLEAN DEFAULT TRUE,
state varchar(120),
title varchar(120) NOT NULL,
views INTEGER
);

CREATE TABLE IF NOT EXISTS requests (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
requester_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
created TIMESTAMP NOT NULL,
status VARCHAR(12) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
title VARCHAR(50) NOT NULL,
pinned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS event_compilations (
compilation_id BIGINT REFERENCES compilations(id),
event_id BIGINT REFERENCES events(id),
CONSTRAINT compilation_event_pk PRIMARY KEY(compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comments (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
author_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
created TIMESTAMP NOT NULL,
status VARCHAR(120) NOT NULL,
text VARCHAR(5000) NOT NULL
);

