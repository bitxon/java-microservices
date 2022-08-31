DROP TABLE IF EXISTS account;
CREATE TABLE account (
	id SERIAL PRIMARY KEY,
	email VARCHAR ( 50 ) NOT NULL,
	first_name VARCHAR ( 50 ) NOT NULL,
	last_name VARCHAR ( 50 ) NOT NULL,
	date_of_birth DATE NOT NULL,
	currency VARCHAR ( 50 ) NOT NULL,
	money_amount INT NOT NULL
);

INSERT INTO account (id, email, first_name, last_name, date_of_birth, currency, money_amount) VALUES
    (DEFAULT, 'alice@mail.com', 'Alice', 'Anderson', '1991-01-21', 'USD', 340),
    (DEFAULT, 'bob@mail.com', 'Bob', 'Brown', '1992-02-22', 'USD', 573),
    (DEFAULT, 'conor@mail.com', 'Conor', 'Carter', '1993-03-23', 'USD', 79),
    (DEFAULT, 'dilan@mail.com', 'Dilan', 'Davis', '1994-04-24', 'USD', 33),
    (DEFAULT, 'eva@mail.com', 'Eva', 'Edwards', '1995-05-25', 'USD', 100),
    (DEFAULT, 'frank@mail.com', 'Frank', 'Fox', '1996-06-26', 'GBP', 80);