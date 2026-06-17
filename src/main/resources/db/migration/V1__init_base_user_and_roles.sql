CREATE TABLE IF NOT EXISTS users (
       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       email varchar(255) NOT NULL UNIQUE,
       first_name varchar(50) NOT NULL,
       password_hash varchar NOT NULL,
       is_active boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_roles (
       user_id BIGINT NOT NULL,
       role varchar(50) NOT NULL
);