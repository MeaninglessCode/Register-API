-- Enable use of type UUID; only needed once for a db
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Transactions table

CREATE TABLE IF NOT EXISTS transactions (
    "id" UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    "cashierid" TEXT NOT NULL REFERENCES employees(employeeid),
    "type" TEXT NOT NULL DEFAULT(''),
    "total" NUMERIC(6, 2) NOT NULL DEFAULT 0,
    "referenceid" UUID,
    "createdon" TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT chk_type CHECK (type IN ('sale', 'return'))
);

-- Index on transactions using lowercase cashierid
CREATE INDEX IF NOT EXISTS ix_transactions_by_cashierid ON transactions
    USING btree(cashierid COLLATE pg_catalog."default");

-- Transaction entires table

CREATE TABLE IF NOT EXISTS transaction_entries (
    "id" UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    "transactionid" UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    "lookupcode" VARCHAR(32) NOT NULL REFERENCES products(lookupcode),
    "price" NUMERIC(6, 2) NOT NULL,
    "discount" NUMERIC(4, 4) NOT NULL,
    "quantity" INT NOT NULL
);

-- Index on transaction entries using lowercase item lookupcode
CREATE INDEX IF NOT EXISTS ix_entries_by_lookupcode ON transaction_entries
    USING btree(lookupcode COLLATE pg_catalog."default");
