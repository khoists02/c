CREATE TABLE IF NOT EXISTS users (
    id uuid not null default uuid_generate_v4() primary key,
    username varchar not null,
    password varchar not null,
    compare_password varchar not null,
    email varchar not null
);
