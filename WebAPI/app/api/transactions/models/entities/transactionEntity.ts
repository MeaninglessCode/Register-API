const uuid = require("uuidv4");
import * as moment from "moment";
import { Transaction } from "../types/transaction";
import { TransactionFieldNames } from "../constants/transactionFieldNames";
import BaseEntity from "../../../models/entities/baseEntity";

export default class TransactionEntity extends BaseEntity {
    private _cashierId: string;
    private _type: string;
    private _total: number;
    private _referenceId: string;
    private _createdOn: moment.Moment;

    constructor(transactionRequest?: Transaction) {
        super(transactionRequest, "transactions");

        this._cashierId = (transactionRequest && transactionRequest.cashierId)
            ? transactionRequest.cashierId : "";
        this._type = (transactionRequest && transactionRequest.type)
            ? transactionRequest.type : "";
        this._total = (transactionRequest && transactionRequest.total)
            ? transactionRequest.total : -1;
        this._referenceId = (transactionRequest && transactionRequest.referenceId)
            ? transactionRequest.referenceId : "";
        this._createdOn = (transactionRequest && transactionRequest.createdOn)
            ? transactionRequest.createdOn : moment();
    }

    public get cashierId(): string { return this._cashierId; }
    public set cashierId(value: string ) {
        if (this._cashierId !== value) {
            this._cashierId = value;
            this.propertyChanged(TransactionFieldNames.TYPE);
        }
    }

    public get type(): string { return this._type; }
    public set type(value: string) {
        if (this._type !== value) {
            this._type = value;
            this.propertyChanged(TransactionFieldNames.TYPE);
        }
    }

    public get total(): number { return this._total; }
    public set total(value: number ) {
        if (this._total !== value) {
            this._total = value;
            this.propertyChanged(TransactionFieldNames.TOTAL);
        }
    }

    public get referenceId(): string { return this._referenceId; }
    public set referenceId(value: string) {
        if (this._referenceId !== value) {
            this._referenceId = value;
            this.propertyChanged(TransactionFieldNames.REFERENCE_ID);
        }
    }

    public get createdOn(): moment.Moment { return this._createdOn; }
    public set createdOn(value: moment.Moment ) {
        if (this._createdOn !== value) {
            this._createdOn = value;
            this.propertyChanged(TransactionFieldNames.CREATED_ON);
        }
    }

    public toJSON(): Transaction {
        return new Transaction(
            super.id, this._cashierId, this._type, this._total, this._referenceId, this._createdOn
        );
    }

    public fillFromRecord(row: any): void {
        super.fillFromRecord(row);

        this._cashierId = row[TransactionFieldNames.CASHIER_ID.toLowerCase()];
        this._type = row[TransactionFieldNames.TYPE];
        this._total = row[TransactionFieldNames.TOTAL];
        this._referenceId = row[TransactionFieldNames.REFERENCE_ID.toLowerCase()];
        this._createdOn = moment.utc(row[TransactionFieldNames.CREATED_ON.toLowerCase()], "YYYY-MM-DD HH:mm:ss");
    }

    public fillRecord(): any {
        let record: any = super.fillRecord();

        record[TransactionFieldNames.CASHIER_ID] = this._cashierId;
        record[TransactionFieldNames.TYPE] = this._type;
        record[TransactionFieldNames.TOTAL] = this._total;
        record[TransactionFieldNames.REFERENCE_ID] = this._referenceId;
        record[TransactionFieldNames.CREATED_ON] = this._createdOn;

        return record;
    }

    public synchronize(transactionRequest: Transaction): void {
        this.cashierId = (transactionRequest) ? transactionRequest.cashierId : "";
        this.type = (transactionRequest) ? transactionRequest.type : "";
        this.total = (transactionRequest) ? transactionRequest.total : -1;
        this.referenceId = (transactionRequest) ? transactionRequest.referenceId : "";
        this.createdOn = (transactionRequest) ? transactionRequest.createdOn : moment();
    }
}