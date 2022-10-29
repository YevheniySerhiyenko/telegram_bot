CREATE TABLE user_stickers (
    id BIGINT NOT NULL,
    action VARCHAR(255) NOT NULL,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    enabled BOOLEAN NOT NULL,
CONSTRAINT pk_user_stickers PRIMARY KEY (id));