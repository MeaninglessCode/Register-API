const uuid = require("uuidv4");
import { Employee } from "../models/types/employee";
import EmployeeDb from "../models/types/employeeDb";
import { StringExtensions } from "../../../extensions/StringExtensions";
import { CommandResponse, IEmployeeDb } from "../../../types";
import EmployeeEntity from "../models/entities/employeeEntity";

export default class QueryUpdate {
    private _employeeId: string;
    private _employeeToSave: Employee;
    private _employeeDb: IEmployeeDb;

    constructor({ employeeId = uuid.empty(), employeeToSave = new Employee(),
        employeeDb = new EmployeeDb() }: any = {}) {
        this._employeeId = employeeId;
        this._employeeToSave = employeeToSave;
        this._employeeDb = employeeDb;
    }

    public get employeeId(): string { return this.employeeId; }
    public set employeeId(value: string) { this._employeeId = value; }

    public get employeeToSave(): Employee { return this._employeeToSave; }
    public set employeeToSave(value: Employee) { this._employeeToSave = value; }

    public get employeeDb(): IEmployeeDb { return this._employeeDb; }
    public set employeeDb(value: IEmployeeDb) { this._employeeDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            if (StringExtensions.isNullOrWhitespace(this._employeeToSave.employeeId))
                reject({ reason: 422, message: "Missing or invalid parameter employeeId", data: {} });

            this._employeeDb.get(this._employeeId).then((entity: EmployeeEntity | undefined) => {
                if (entity) {
                    entity.synchronize(this._employeeToSave);
                    entity.save().then((value: string) => {
                        resolve({ status: 200, message: "Success", data: entity.toJSON() });
                    },
                    (reason: any) => {
                        reject({ status: 500, message: reason.message, data: {} });
                    });
                }
                else
                    reject({ reason: 404, message: "Employee not found", data: {} });
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: {} });
            });
        });
    }
}