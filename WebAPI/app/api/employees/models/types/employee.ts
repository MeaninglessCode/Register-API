const uuid = require("uuidv4");
import * as moment from "moment";
import { MappedEntityBaseType } from "../../../models/types/mappedEntityBaseType";

export class Employee extends MappedEntityBaseType {
    public id: string;
    public employeeId: string;
    public first: string;
    public last: string;
    public role: string;
    public password: string;
    public active: boolean;
    public manager: string;
    public created: moment.Moment;

    constructor(id = uuid.empty(), employeeId = "", first = "", last = "", role = "", password = "",
        active = false, manager = uuid.empty(), created = moment()) {
        super(id);
        this.id = id;
        this.employeeId = employeeId;
        this.first = first;
        this.last = last;
        this.role = role;
        this.password = password;
        this.active = active;
        this.manager = manager;
        this.created = created;
    }
}