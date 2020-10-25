import { Employee } from "../models/types/employee";
import EmployeeDb from "../models/types/employeeDb";
import { StringExtensions } from "../../../extensions/StringExtensions";
import { CommandResponse, IEmployeeDb } from "../../../types";
import EmployeeEntity from "../models/entities/employeeEntity";

export default class QueryCreate {
    private _employeeToSave: Employee;
    private _employeeDb: IEmployeeDb;

    constructor({ employeeToSave = new Employee(), employeeDb = new EmployeeDb() }: any = {}) {
        this._employeeToSave = employeeToSave;
        this._employeeDb = employeeDb;
    }

    public get employeeToSave(): Employee { return this._employeeToSave; }
    public set employeeToSave(value: Employee) { this._employeeToSave = value; }

    public get employeeDb(): IEmployeeDb { return this._employeeDb; }
    public set employeeDb(value: IEmployeeDb) { this._employeeDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            if (StringExtensions.isNullOrWhitespace(this._employeeToSave.employeeId))
                reject({ status: 422, message: "Missing or invalid parameter: employeeId", data: {} });

            this._employeeDb.byEmployeeId(this._employeeToSave.employeeId)
            .then((entity: EmployeeEntity | undefined) => {
                if (!entity) {
                    let newProduct = new EmployeeEntity(this._employeeToSave);

                    newProduct.save().then((value: string) => {
                        resolve({ status: 200, message: "Employee created", data: newProduct.toJSON() });
                    },
                    (reason: any) => {
                        reject(reason);
                    });
                }
                else
                    reject({ status: 409, message: "employeeId already in use", data: {} });
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: {} });
            });
        });
    }
}