import { IBaseDb } from "../../../types";
import db = require("./dbConnection");
import BaseEntity from "../entities/baseEntity";

export default abstract class DbBase<T extends BaseEntity> implements IBaseDb<T> {
    private _tableName: string;
    private static _invalidIndex: number = -1;
    private static _existsSelectCount: string = "1";

    constructor(tableName: string) {
        this._tableName = tableName;
    }

    public get tableName(): string { return this._tableName; }

    protected abstract createOne(row: any): T;

    public all(): Promise<T[]> {
        let query = `SELECT * FROM ${this._tableName}`;

        return new Promise((resolve, reject) => {
            db.any(query).then((data: any) => {
                let results: T[] = [];
                for (let i = 0; i < data.length; i++)
                    results.push(this.createOne(data[i]));
                resolve(results);
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public get(id: string): Promise<T | undefined> {
        let query = `SELECT * FROM ${this._tableName} WHERE id=\${id}`;
        return this.firstOrDefaultWhere(query, { "id" : id });
    }

    public getMany(query: string, values: any): Promise<T[]> {
        return new Promise((resolve, reject) => {
            db.many(query, values).then((data: any) => {
                let results: T[] = [];
                for (let i = 0; i < data.length; i++)
                    results.push(this.createOne(data[i]));
                resolve(results);
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public getRowCount(): Promise<number> {
        let query = `SELECT COUNT(*) FROM ${this._tableName}`;

        return new Promise((resolve, reject) => {
            db.any(query).then((data: any) => {
                resolve(data["count"]);
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public exists(id: string): Promise<boolean> {
        let query = `SELECT EXISTS (SELECT ${DbBase._existsSelectCount} FROM ${this._tableName} WHERE id=\${id})`;

        return new Promise((resolve, reject) => {
            db.one(query, { "id" : id }).then((data: any) => {
                resolve(data && (data.exists === true));
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public inRange(limit: number, offset: number): Promise<T[]> {
        let query = `SELECT * FROM ${this._tableName} LIMIT ${limit} OFFSET ${offset}`;

        return new Promise((resolve, reject) => {
            db.any(query).then((data: any) => {
                let results: T[] = [];
                for (let i = 0; i < data.length; i++)
                    results.push(this.createOne(data[i]));
                resolve(results);
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public saveMany(toSave: T[]): Promise<null> {
        return new Promise((resolve, reject) => {
            db.tx((t: any) => {
                let saveCommands: Promise<string>[] = [];
                toSave.forEach((e) => {
                    saveCommands.push(e.save(t));
                });
                return t.batch(saveCommands);
            }).then((data: any) => {
                resolve();
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public deleteMany(toDelete: T[]): Promise<null> {
        return new Promise((resolve, reject) => {
            db.tx((t: any) => {
                let deleteCommands: Promise<null>[] = [];
                toDelete.forEach((e) => {
                    deleteCommands.push(e.delete(t));
                });
                return t.batch(deleteCommands);
            }).then((data: any) => {
                resolve();
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public connectAndRun(context: T, action: (self: T, connection: any) => Promise<any>): Promise<any> {
        if (action) {
            return new Promise((resolve, reject) => {
                action(context, db).then((data: any) => {
                    resolve(data);
                },
                (reason: any) => {
                    reject(reason);
                });
            });
        }
        else
            return new Promise((resolve, reject) => { resolve(); });
    }

    protected firstOrDefaultWhere(query: string, values: any = []): Promise<T | undefined> {
        return new Promise((resolve, reject) => {
            db.oneOrNone(query, values).then((data: any) => {
                resolve((data) ? this.createOne(data) : undefined);
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }
}