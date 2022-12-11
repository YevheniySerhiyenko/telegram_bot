CREATE TABLE IF NOT EXISTS expenses
(
    id        serial PRIMARY KEY,
    category  varchar   NOT NULL,
    sum       decimal   NOT NULL,
    date_time timestamp NOT NULL,
    user_id   bigint    NOT NULL
);


