const bcrypt = require("bcrypt");
import { CommandResponse, IEmployeeDb } from "../../../types";
import EmployeeEntity from "../models/entities/employeeEntity";
import EmployeeDb from "../models/types/employeeDb";

export default class QueryLogin {
    private _employeeId: string;
    private _employeePassword: string;
    private _employeeDb: IEmployeeDb;

    constructor({ employeeId = "", employeePassword = "", employeeDb = new EmployeeDb() }: any = {}) {
        this._employeeId = employeeId;
        this._employeePassword = employeePassword;
        this._employeeDb = employeeDb;
    }

    public get employeeId(): string { return this._employeeId; }
    public set employeeId(value: string) { this._employeeId = value; }

    public get employeePassword(): string { return this._employeePassword; }
    public set employeePassword(value: string) { this._employeePassword = value; }

    public get employeeDb(): IEmployeeDb { return this._employeeDb; }
    public set employeeDb(value: IEmployeeDb) { this._employeeDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._employeeDb.byEmployeeId(this._employeeId).then((entity: EmployeeEntity | undefined) => {
                if (entity) {
                    bcrypt.compare(this._employeePassword, entity.password, (err: any, res: any) => {
                        if (res) {
                            entity.active = true;
                            entity.save();
                            resolve({ status: 200, message: "Login successful", data: entity.toJSON() });
                        }
                        else {
                            reject({ status: 401, message: "Invalid password", data: {} });
                        }
                    });
                }
                else {
                    reject({ status: 404, message: "Employee not found", data: {} });
                }
            });
        });
    }
}