create table if not exists categories(
    id serial not null,
    name varchar not null,
    primary key (id,name)
);