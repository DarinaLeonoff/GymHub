CREATE IF NOT EXISTS users (
       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       email varchar(255) NOT NULL UNIQUE,
       first_name varchar(50) NOT NULL,
       password_hash varchar NOT NULL,
       is_active boolean NOT NULL DEFAULT TRUE
);


CREATE IF NOT EXISTS roles (
       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       name varchar(20) NOT NULL UNIQUE
);

CREATE IF NOT EXISTS user_roles (
       user_id BIGINT NOT NULL,
       role_id INT NOT NULL,
       PRIMARY KEY (user_id, role_id),
       CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users(id),
       CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles(name) VALUES ('ROLE_USER');