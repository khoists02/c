CREATE TABLE IF NOT EXISTS users (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    username varchar NOT NULL,
    password varchar NOT NULL,
    compare_password varchar NOT NULL,
    email varchar NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_un UNIQUE (username)
);
