import * as restify from "restify";
import DbBase from "../../../models/db/dbBase";
import EmployeeEntity from "../entities/employeeEntity";
import db = require("../../../models/db/dbConnection");
import { IEmployeeDb } from "../../../../types";
import { EmployeeFieldNames } from "../constants/employeeFieldNames";

export default class EmployeeDb extends DbBase<EmployeeEntity> implements IEmployeeDb {
    constructor() {
        super("employees");
    }

    protected createOne(row: any): EmployeeEntity {
        let entity = new EmployeeEntity();
        entity.fillFromRecord(row);
        return entity;
    }

    public byEmployeeId(employeeId: string): Promise<EmployeeEntity | undefined> {
        let query = `SELECT * FROM ${this.tableName} WHERE ${EmployeeFieldNames.EMPLOYEE_ID}=\${employeeId}`;
        return this.firstOrDefaultWhere(query, { "employeeId" : employeeId });
    }
}