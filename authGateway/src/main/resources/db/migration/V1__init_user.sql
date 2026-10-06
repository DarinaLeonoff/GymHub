CREATE TABLE IF NOT EXISTS users
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    created       TIMESTAMP DEFAULT NOW(),
    password_hash VARCHAR NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT TRUE,
    acc_type      VARCHAR(50) NOT NULL,
    user_role     VARCHAR(50) NOT NULL
    );