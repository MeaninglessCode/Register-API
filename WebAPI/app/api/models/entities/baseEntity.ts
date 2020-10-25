const uuid = require("uuidv4");
import * as moment from "moment";
import { IDatabase } from "pg-promise";
import db = require("../db/dbConnection");
import { MappedEntityBaseType } from "../types/mappedEntityBaseType";

export default abstract class BaseEntity {
    private _id: string;
    private _isNew: boolean;
    private _isDirty: boolean;
    private _tableName: string;
    private _toUpdateFieldNames: string[];

    constructor(request?: MappedEntityBaseType, tableName: string = "") {
        this._isNew = true;
        this._isDirty = true;
        this._toUpdateFieldNames = [];

        this._tableName = tableName;
        this._id = (request) ? request.id : uuid.empty();
    }

    public get id(): string { return this._id; }
    public set id(value: string) { this._id = value; }

    public get isNew(): boolean { return this._isNew; }

    public get isDirty(): boolean { return this._isDirty; }

    public get tableName(): string { return this._tableName; }

    public toJSON() {
        return { id: this._id };
    }

    public fillFromRecord(row: any): void {
        this._isNew = false;
        this._isDirty = false;

        this._id = row["id"];
    }

    protected fillRecord(): any {
        return ((this._id && (this._id !== uuid.empty())) ? { "id": this._id } : {});
    }

    protected propertyChanged(propertyName: string): void {
        if (!this._isDirty)
            this._isDirty = true;
        if (this._toUpdateFieldNames.indexOf(propertyName) < 0)
            this._toUpdateFieldNames.push(propertyName);
    }

    public save(): Promise<string>;
    public save(conn: IDatabase<any>): Promise<string>;
    public save(conn?: IDatabase<any>): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            this.saveAction((conn) ? conn : db).then(() => {
                this._isNew = false;
                this._isDirty = false;
                this._toUpdateFieldNames = [];
                resolve(this._id);
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    public delete(): Promise<null>;
    public delete(conn: IDatabase<any>): Promise<null>;
    public delete(conn?: IDatabase<any>): Promise<null> {
        if (this._isNew)
            return Promise.resolve<null>(null);

        let query = `DELETE FROM ${this._tableName} WHERE id=\${id}`;
        conn = (conn) ? conn : db;
        return conn.none(query, { "id": this._id });
    }

    private saveAction(conn: IDatabase<any>): Promise<null> {
        if (this._isNew)
            return this.insertRecord(conn);
        else if (this._isDirty && (this._toUpdateFieldNames.length > 0))
            return this.updateRecord(conn);
        else
            return Promise.resolve(null);
    }

    private insertRecord(conn: IDatabase<any>): Promise<null> {
        let insertRecord: any = this.fillRecord();
        let query = `INSERT INTO ${this._tableName} (`;

        let divider: boolean = false;
        for (let prop in insertRecord) {
            if (divider) {
                query += ", ";
            }
            else {
                divider = true;
            }
            query += prop;
        }
        query += ") VALUES (";

        divider = false;
        let i = 1;
        for (let prop in insertRecord) {
            if (divider) {
                query += ", ";
            }
            else {
                divider = true;
            }
            query += `\${${prop}}`;
            i++;
        }
        query += ") RETURNING id";

        return new Promise<null>((resolve, reject) => {
            conn.one(query, insertRecord).then((data: any) => {
                if (data) {
                    this._id = data["id"];
                }
                resolve();
            },
            (reason: any) => {
                reject(reason);
            });
        });
    }

    private updateRecord(conn: IDatabase<any>): Promise<null> {
        let record: any = this.fillRecord();
        let query = `UPDATE ${this._tableName} SET `;

        query += this._toUpdateFieldNames.map((value: string) => { return `${value}=\${${value}}`; }).join();
        query += " WHERE id=${id}";

        return conn.none(query, record);
    }
}