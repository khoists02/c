CREATE TABLE IF NOT EXISTS playstations (

    id uuid not null default uuid_generate_v4() primary key,
    smoke int not null,
    matu int not null,
    learning int not null,
    cleaning int not null,
    type varchar(50) not null,
    "date" timestamp default now()
);
