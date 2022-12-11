ALTER TABLE users
    ADD COLUMN password varchar(255);
ALTER TABLE users
    ADD COLUMN is_logined boolean DEFAULT FALSE;
ALTER TABLE users
    ADD COLUMN enable_password boolean DEFAULT FALSE;
ALTER TABLE users
    ADD COLUMN last_action_time timestamp DEFAULT NOW();