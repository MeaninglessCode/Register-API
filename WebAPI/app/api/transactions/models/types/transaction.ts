const uuid = require("uuidv4");
import * as moment from "moment";
import {MappedEntityBaseType} from "../../../models/types/mappedEntityBaseType";

export class Transaction extends MappedEntityBaseType {
    public id: string;
    public cashierId: string;
    public type: string;
    public total: number;
    public referenceId: string;
    public createdOn: moment.Moment;

    constructor(id = uuid.empty(), cashierId = "", type = "", total = 0, referenceId = uuid.empty(),
        createdOn = moment()) {
        super(id);
        this.id = id;
        this.cashierId = cashierId;
        this.type = type;
        this.total = total;
        this.referenceId = referenceId;
        this.createdOn = createdOn;
    }
}