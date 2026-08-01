-- transaction module

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    category VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    occurred_on DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_transactions_occurred_on ON transactions (occurred_on);
CREATE INDEX IF NOT EXISTS idx_transactions_category ON transactions (category);

-- budget module

CREATE TABLE IF NOT EXISTS category_budgets (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(100) NOT NULL UNIQUE,
    monthly_limit NUMERIC(12, 2) NOT NULL CHECK (monthly_limit > 0)
);

CREATE TABLE IF NOT EXISTS category_spend (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    year_month VARCHAR(7) NOT NULL,
    spent_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    UNIQUE (category, year_month)
);

-- summary module

CREATE TABLE IF NOT EXISTS account_balance (
    id BIGINT PRIMARY KEY,
    balance NUMERIC(14, 2) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

INSERT INTO account_balance (id, balance)
VALUES (1, 0)
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS monthly_summary (
    id BIGSERIAL PRIMARY KEY,
    year_month VARCHAR(7) NOT NULL UNIQUE,
    total_income NUMERIC(14, 2) NOT NULL DEFAULT 0,
    total_expense NUMERIC(14, 2) NOT NULL DEFAULT 0
);
