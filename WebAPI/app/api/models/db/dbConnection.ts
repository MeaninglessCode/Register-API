import * as pgPromise from "pg-promise";
import { IMain, IDatabase } from "pg-promise";
import { settings } from "../../../config/config";

const pgp: IMain = pgPromise({});
const db: IDatabase<any> = pgp(process.env.DATABASE_URL || "");

export = db;