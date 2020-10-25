-- Enable use of type UUID; only needed once for a db
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Employees table

CREATE TABLE IF NOT EXISTS employees (
    "id" UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    "employeeid" TEXT NOT NULL UNIQUE,
    "first" TEXT NOT NULL DEFAULT(''),
    "last" TEXT NOT NULL DEFAULT(''),    
    "role" TEXT NOT NULL DEFAULT(''),
    "password" TEXT NOT NULL DEFAULT(''),
    "active" BOOL NOT NULL DEFAULT FALSE,
    "manager" UUID,
    "created" TIMESTAMP NOT NULL DEFAULT now()
);

-- Index on employees table using the lowercase employeeid
CREATE INDEX IF NOT EXISTS ix_employees_by_employeeid ON employees
    USING btree(employeeid COLLATE pg_catalog."default");
