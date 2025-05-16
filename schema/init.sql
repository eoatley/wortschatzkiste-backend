CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username TEXT UNIQUE NOT NULL,
                       created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE words (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                       german TEXT NOT NULL,
                       english TEXT NOT NULL,
                       added_at TIMESTAMP DEFAULT NOW(),
                       UNIQUE(user_id, german)
);

CREATE TABLE tests (
                       id SERIAL PRIMARY KEY,
                       word_id INTEGER REFERENCES words(id) ON DELETE CASCADE,
                       tested_at TIMESTAMP DEFAULT NOW(),
                       success BOOLEAN NOT NULL
);

CREATE INDEX idx_words_user_id ON words(user_id);
CREATE INDEX idx_tests_word_id ON tests(word_id);
