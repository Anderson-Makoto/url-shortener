CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    short_url VARCHAR(10) NOT NULL UNIQUE,
    original_url VARCHAR(10) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER REFERENCES users(id),
    click_count INTEGER DEFAULT 0
)