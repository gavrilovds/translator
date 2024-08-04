CREATE TABLE IF NOT EXISTS translation
(
    id                 SERIAL PRIMARY KEY,
    user_ip            VARCHAR(45) NOT NULL,
    before_translation TEXT        NOT NULL,
    after_translation  TEXT        NOT NULL,
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_user_ip ON translation (user_ip);