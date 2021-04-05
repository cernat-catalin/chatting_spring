# Database generation

## User table

DDL
```sqlite
CREATE TABLE user
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    username text NOT NULL,
    password text NOT NULL,
    UNIQUE (username)
);

CREATE TABLE user_statistics
(
    user_id    INTEGER PRIMARY KEY,
    n_logins   INTEGER DEFAULT 0,
    n_messages INTEGER DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user (id)
);
```