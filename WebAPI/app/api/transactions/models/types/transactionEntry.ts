const uuid = require("uuidv4");
import { MappedEntityBaseType } from "../../../models/types/mappedEntityBaseType";

export class TransactionEntry extends MappedEntityBaseType {
    public id: string;
    public transactionId: string;
    public lookupCode: string;
    public price: number;
    public discount: number;
    public quantity: number;

    constructor(id = uuid.empty(), transactionId = uuid.empty(), lookupCode = "", price = 0,
        discount = 0, quantity = 0) {
        super(id);
        this.id = id;
        this.transactionId = transactionId;
        this.lookupCode = lookupCode;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }
}