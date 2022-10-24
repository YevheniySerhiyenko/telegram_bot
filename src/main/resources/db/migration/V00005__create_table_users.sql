create table if not exists users(
  chat_id bigint primary key,
  name varchar(256) not null
)