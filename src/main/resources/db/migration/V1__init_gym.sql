CREATE TABLE IF NOT EXISTS gym (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    address VARCHAR(500) NOT NULL,
    tel VARCHAR(12) NOT NULL,
    email VARCHAR(254) NOT NULL
);

INSERT INTO gym (id, name, description, address, tel, email)
    OVERRIDING SYSTEM VALUE
VALUES (1, 'Base Gym', 'Базовое описание зала для тестов обновления', 'г. Москва, ул. Ленина, д. 1', '+79991112233', 'base@gymhub.ru');
