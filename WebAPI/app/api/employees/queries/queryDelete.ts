const uuid = require("uuidv4");
import { CommandResponse, IEmployeeDb } from "../../../types";
import EmployeeEntity from "../models/entities/employeeEntity";
import EmployeeDb from "../models/types/employeeDb";

export default class QueryDelete {
    private _employeeId: string;
    private _employeeDb: IEmployeeDb;

    constructor({ employeeId = uuid.empty(), employeeDb = new EmployeeDb() }: any = {}) {
        this._employeeId = employeeId;
        this._employeeDb = employeeDb;
    }

    public get employeeId(): string { return this._employeeId; }
    public set employeeId(value: string) { this._employeeId = value; }

    public get employeeDb(): IEmployeeDb { return this._employeeDb; }
    public set employeeDb(value: IEmployeeDb) { this._employeeDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._employeeDb.get(this._employeeId).then((entity: EmployeeEntity | undefined) => {
                if (entity) {
                    entity.delete().then(() => {
                        resolve({ status: 200, message: "Success", data: {} });
                    },
                    (reason: any) => {
                        reject({ status: 500, message: reason.message, data: {} });
                    });
                }
                else
                    reject({ status: 404, message: "Employee not found", data: {} });
            });
        });
    }
}