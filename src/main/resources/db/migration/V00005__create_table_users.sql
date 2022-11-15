create table if not exists users(
  user_id bigint primary key,
  name varchar(256) not null
)