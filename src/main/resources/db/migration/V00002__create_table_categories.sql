create table if not exists categories(
    id serial primary key,
    name varchar unique not null
);