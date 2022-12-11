
create table if not exists expenses(
    id serial primary key,
    category varchar not null,
    sum decimal not null,
    date_time timestamp not null,
    user_id bigint not null
);


