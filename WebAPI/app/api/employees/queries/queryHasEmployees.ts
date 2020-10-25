const uuid = require("uuidv4");
import { CommandResponse, IEmployeeDb } from "../../../types";
import EmployeeDb from "../models/types/employeeDb";

export default class QueryHasEmployees {
    private _employeeDb: IEmployeeDb;

    constructor({ employeeDb = new EmployeeDb() }: any = {}) {
        this._employeeDb = employeeDb;
    }

    public get employeeDb(): IEmployeeDb { return this._employeeDb; }
    public set employeeDb(value: IEmployeeDb) { this._employeeDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._employeeDb.getRowCount().then((rowCount: number) => {
                if (rowCount <= 0) {
                    resolve({ status: 404, message: "No rows in table", data: {} });
                }
                else {
                    resolve({ status: 200, message: "Success", data: {} });
                }
            });
        });
    }
}