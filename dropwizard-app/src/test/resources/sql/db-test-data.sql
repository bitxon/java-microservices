DROP TABLE IF EXISTS account;
CREATE TABLE account (
	id SERIAL PRIMARY KEY,
	email VARCHAR ( 50 ) NOT NULL,
	currency VARCHAR ( 50 ) NOT NULL,
	money_amount INT NOT NULL
);

INSERT INTO account (id, email, currency, money_amount) VALUES
    (DEFAULT, 'alice@mail.com', 'USD', 340),
    (DEFAULT, 'bob@mail.com', 'USD', 573),
    (DEFAULT, 'conor@mail.com', 'USD', 79),
    (DEFAULT, 'dilan@mail.com', 'USD', 33),
    (DEFAULT, 'eva@mail.com', 'USD', 100);