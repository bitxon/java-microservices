DROP TABLE IF EXISTS account;
CREATE TABLE account (
	id SERIAL PRIMARY KEY,
	email VARCHAR ( 50 ) NOT NULL,
	first_name VARCHAR ( 50 ) NOT NULL,
	last_name VARCHAR ( 50 ) NOT NULL,
	currency VARCHAR ( 50 ) NOT NULL,
	money_amount INT NOT NULL
);

INSERT INTO account (id, email, first_name, last_name, currency, money_amount) VALUES
    (DEFAULT, 'alice@mail.com', 'Alice', 'Anderson', 'USD', 340),
    (DEFAULT, 'bob@mail.com', 'Bob', 'Brown','USD', 573),
    (DEFAULT, 'conor@mail.com', 'Conor', 'Carter', 'USD', 79),
    (DEFAULT, 'dilan@mail.com', 'Dilan', 'Davis', 'USD', 33),
    (DEFAULT, 'eva@mail.com', 'Eva', 'Edwards', 'USD', 100),
    (DEFAULT, 'frank@mail.com', 'Frank', 'Fox', 'GBP', 80);