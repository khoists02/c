CREATE TABLE IF NOT EXISTS permissions (
    id uuid not null default uuid_generate_v4() primary key,
    name varchar not null,
    code varchar not null
);
