CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE ticket (
    id SERIAL PRIMARY KEY,
    row INT NOT NULL,
    col INT NOT NULL,
    price INT NOT NULL CHECK (price > 0),
    status VARCHAR NOT NULL GENERATED ALWAYS AS (CASE WHEN account_id IS NULL THEN 'Free' ELSE 'Taken' END) STORED,
    account_id INT REFERENCES account(id),
    constraint uniqueTicket unique (row, col)
);
