CREATE TABLE sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE,
    token TEXT NOT NULL UNIQUE,
    created_at TEXT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_sessions_token ON sessions(token);

CREATE TABLE blocks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    metadata_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    sequence_id INTEGER NOT NULL,
    size INTEGER NOT NULL,
    checksum TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (metadata_id) REFERENCES metadata(id) ON DELETE CASCADE,
    UNIQUE(user_id, metadata_id, sequence_id)
);

CREATE INDEX idx_blocks_metadata_sequence ON blocks(metadata_id, sequence_id);

CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE metadata (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    filename TEXT NOT NULL,
    owner_id INTEGER NOT NULL,
    path TEXT NOT NULL UNIQUE,
    block_count INTEGER NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(owner_id, filename)
);

CREATE INDEX idx_metadata_owner_filename ON metadata(owner_id, filename);
