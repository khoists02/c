CREATE TABLE IF NOT EXISTS permissions (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    groupKey varchar NOT NULL,
    code varchar NOT NULL,
    CONSTRAINT permissions_pk PRIMARY KEY (id),
    CONSTRAINT permissions_un UNIQUE (code)
);
