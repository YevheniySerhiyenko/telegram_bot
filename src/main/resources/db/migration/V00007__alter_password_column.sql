alter table users add column password varchar(255);
alter table users add column is_logined boolean default false;
alter table users add column enable_password boolean default false;
alter table users add column last_action_time timestamp default now();