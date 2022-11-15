CREATE TABLE stickers (
    id BIGSERIAL NOT NULL,
    token VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    user_id BIGINT,
    CONSTRAINT pk_stickers PRIMARY KEY (id)
);

ALTER TABLE stickers ADD CONSTRAINT FK_STICKERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);