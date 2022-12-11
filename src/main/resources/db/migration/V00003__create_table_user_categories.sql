CREATE TABLE IF NOT EXISTS user_categories
(
    id       serial PRIMARY KEY,
    category varchar(256) NOT NULL,
    user_id  decimal      NOT NULL
);