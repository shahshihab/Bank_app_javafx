-- Create a table for bank accounts
CREATE TABLE BankAccount (
    AccountNumber VARCHAR(20) PRIMARY KEY,
    Pin VARCHAR(10) NOT NULL,
    Balance DECIMAL(18,2) NOT NULL DEFAULT 0.00
);
