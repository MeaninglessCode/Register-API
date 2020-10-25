const uuid = require("uuidv4");
import * as moment from "moment";
import BaseEntity from "../../../models/entities/baseEntity";
import { Employee } from "../types/employee";
import { EmployeeFieldNames } from "../constants/employeeFieldNames";

export default class EmployeeEntity extends BaseEntity {
    private _employeeId: string;
    private _first: string;
    private _last: string;
    private _role: string;
    private _password: string;
    private _active: boolean;
    private _manager: string;
    private _created: moment.Moment;

    constructor(employeeRequest?: Employee) {
        super(employeeRequest, "employees");
        this._employeeId = (employeeRequest && employeeRequest.employeeId)
            ? employeeRequest.employeeId : "";
        this._first = (employeeRequest && employeeRequest.first)
            ? employeeRequest.first : "";
        this._last = (employeeRequest && employeeRequest.last)
            ? employeeRequest.last : "";
        this._role = (employeeRequest && employeeRequest.role)
            ? employeeRequest.role : "";
        this._password = (employeeRequest && employeeRequest.password)
            ? employeeRequest.password : "";
        this._active = (employeeRequest)
            ? employeeRequest.active : false;
        this._manager = (employeeRequest && employeeRequest.manager)
            ? employeeRequest.manager : uuid.empty();
        this._created = (employeeRequest && employeeRequest.created)
            ? employeeRequest.created : moment();
    }

    public get employeeID(): string { return this._employeeId; }
    public set employeeID(value: string) {
        if (this._employeeId !== value) {
            this._employeeId = value;
            this.propertyChanged(EmployeeFieldNames.EMPLOYEE_ID);
        }
    }

    public get first(): string { return this._first; }
    public set first(value: string) {
        if (this._first !== value) {
            this._first = value;
            this.propertyChanged(EmployeeFieldNames.FIRST_NAME);
        }
    }

    public get last(): string { return this._last; }
    public set last(value: string) {
        if (this._last !== value) {
            this._last = value;
            this.propertyChanged(EmployeeFieldNames.LAST_NAME);
        }
    }

    public get role(): string { return this._role; }
    public set role(value: string) {
        if (this._role !== value) {
            this._role = value;
            this.propertyChanged(EmployeeFieldNames.ROLE);
        }
    }

    public get password(): string { return this._password; }
    public set password(value: string) {
        if (this._password !== value) {
            this._password = value;
            this.propertyChanged(EmployeeFieldNames.PASSWORD);
        }
    }

    public get active(): boolean { return this._active; }
    public set active(value: boolean) {
        if (this._active !== value) {
            this._active = value;
            this.propertyChanged(EmployeeFieldNames.ACTIVE);
        }
    }

    public get manager(): string { return this._manager; }
    public set manager(value: string) {
        if (this._manager !== value) {
            this._manager = value;
            this.propertyChanged(EmployeeFieldNames.MANAGER);
        }
    }

    public get created(): moment.Moment {return this._created; }
    public set created(value: moment.Moment) {
        if (this._created !== value) {
            this._created = value;
            this.propertyChanged(EmployeeFieldNames.CREATED);
        }
    }

    public toJSON(): Employee {
        return new Employee(super.id, this._employeeId, this._first, this._last, this._role,
            this._password, this._active, this._manager, this._created);
    }

    public fillFromRecord(row: any): void {
        super.fillFromRecord(row);

        this._employeeId = row[EmployeeFieldNames.EMPLOYEE_ID.toLowerCase()];
        this._first = row[EmployeeFieldNames.FIRST_NAME];
        this._last = row[EmployeeFieldNames.LAST_NAME];
        this._role = row[EmployeeFieldNames.ROLE];
        this._password = row[EmployeeFieldNames.PASSWORD];
        this._active = row[EmployeeFieldNames.ACTIVE];
        this._manager = row[EmployeeFieldNames.MANAGER];
        this._created = moment.utc(row[EmployeeFieldNames.CREATED], "YYYY-MM-DD HH:mm:ss");
    }

    public fillRecord(): any {
        let record: any = super.fillRecord();

        record[EmployeeFieldNames.EMPLOYEE_ID] = this._employeeId;
        record[EmployeeFieldNames.FIRST_NAME] = this._first;
        record[EmployeeFieldNames.LAST_NAME] = this._last;
        record[EmployeeFieldNames.ROLE] = this._role;
        record[EmployeeFieldNames.PASSWORD] = this._password;
        record[EmployeeFieldNames.ACTIVE] = this._active;
        record[EmployeeFieldNames.MANAGER] = this._manager;
        record[EmployeeFieldNames.CREATED] = this._created;

        return record;
    }

    public synchronize(employeeRequest: Employee): void {
        this.employeeID = (employeeRequest) ? employeeRequest.employeeId : "";
        this.first = (employeeRequest) ? employeeRequest.first : "";
        this.last = (employeeRequest) ? employeeRequest.last : "";
        this.role = (employeeRequest) ? employeeRequest.role : "";
        this.password = (employeeRequest) ? employeeRequest.password : "";
        this.active = (employeeRequest) ? employeeRequest.active : false;
        this.manager = (employeeRequest) ? employeeRequest.manager : "";
        this.created = (employeeRequest) ? employeeRequest.created : moment();
    }
}