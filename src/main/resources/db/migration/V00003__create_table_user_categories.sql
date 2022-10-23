create table if not exists user_categories(
    id serial primary key,
    category varchar not null,
    user_id decimal not null
);