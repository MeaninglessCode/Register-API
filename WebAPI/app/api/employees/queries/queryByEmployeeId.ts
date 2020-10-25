import { CommandResponse, IEmployeeDb } from "../../../types";
import EmployeeEntity from "../models/entities/employeeEntity";
import EmployeeDb from "../models/types/employeeDb";


export default class QueryByEmployeeId {
    private _employeeId: string;
    private _employeeDb: IEmployeeDb;

    constructor({ employeeId = "", employeeDb = new EmployeeDb() }: any = {}) {
        this._employeeId = employeeId;
        this._employeeDb = employeeDb;
    }

    public get employeeId(): string { return this._employeeId; }
    public set employeeId(value: string) { this._employeeId = value; }

    public get employeeDb(): IEmployeeDb { return this._employeeDb; }
    public set employeeDb(value: IEmployeeDb) { this._employeeDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._employeeDb.byEmployeeId(this._employeeId).then((entity: EmployeeEntity | undefined) => {
                if (entity)
                    resolve({ status: 200, message: "Success", data: entity.toJSON() });
                else
                    reject({ status: 404, message: "Employee not found", data: {} });
            },
            (reason: any) => {
                reject({ status: 400, message: reason.message, data: {} });
            });
        });
    }
}