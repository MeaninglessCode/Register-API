const uuid = require("uuidv4");
import * as moment from "moment";
import {MappedEntityBaseType} from "../../../models/types/mappedEntityBaseType";

export class Product extends MappedEntityBaseType {
    public id: string;
    public lookupCode: string;
    public name: string;
    public description: string;
    public price: number;
    public inventory: number;
    public active: boolean;
    public createdOn: moment.Moment;
    public lastUpdate: moment.Moment;

    constructor(id = uuid.empty(), lookupCode = "", name = "", description = "", price = -1,
        inventory = -1, active = true, createdOn = moment(), lastUpdate = moment()) {
        super(id);
        this.id = id;
        this.lookupCode = lookupCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.active = active;
        this.createdOn = createdOn;
        this.lastUpdate = lastUpdate;
    }
}