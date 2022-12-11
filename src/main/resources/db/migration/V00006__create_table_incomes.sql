CREATE TABLE incomes
(
    id          BIGINT                      NOT NULL,
    sum         DECIMAL                     NOT NULL,
    income_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id     BIGINT                      NOT NULL,
    CONSTRAINT pk_incomes PRIMARY KEY (id)
);