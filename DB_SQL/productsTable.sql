-- Enable use of type UUID; only needed once for a db
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Products table

CREATE TABLE IF NOT EXISTS products (
    "id" UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    "lookupcode" VARCHAR(32) NOT NULL UNIQUE,
    "name" TEXT NOT NULL DEFAULT(''),
    "description" TEXT NOT NULL DEFAULT(''),
    "price" NUMERIC(6, 2) NOT NULL DEFAULT 0,
    "inventory" INT NOT NULL DEFAULT 0,
    "active" BOOL NOT NULL DEFAULT TRUE,
    "createdon" TIMESTAMP NOT NULL DEFAULT now(),
    "lastupdate" TIMESTAMP NOT NULL DEFAULT now()
);

-- Index on products table using the lowercase lookupcode
CREATE INDEX IF NOT EXISTS ix_products_bylookupcode ON products
	USING btree(lookupcode COLLATE pg_catalog."default");
-- SHOW lc_collate; to view pg_catalog."default" value
