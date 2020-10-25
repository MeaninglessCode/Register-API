import { Config } from "../types";

export let settings: Config = {
    name: "register-service",
    version: "0.0.1",
    port: (isNaN(Number(process.env.PORT)) ? 15150 : Number(process.env.PORT)),
    env: (process.env.NODE_ENV || "dev"),
    db: {
        host: (process.env.PGHOST || "localhost"),
        port: (isNaN(Number(process.env.PGPORT)) ? 5432 : Number(process.env.PGPORT)),
        database: (process.env.PGDATABASE || "regserv"),
        user: (process.env.PGUSER || "registerappadmin"),
        password: (process.env.PGPASSWORD || "@irtime0")
    }
};
