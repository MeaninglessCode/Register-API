import BaseEntity from "../../../models/entities/baseEntity";
import { TransactionEntry } from "../types/transactionEntry";
import { TransactionEntryFieldNames } from "../constants/transactionEntryFieldNames";

export default class TransactionEntryEntity extends BaseEntity {
    private _transactionId: string;
    private _lookupCode: string;
    private _price: number;
    private _discount: number;
    private _quantity: number;

    constructor (transactionEntryRequest?: TransactionEntry) {
        super(transactionEntryRequest, "transaction_entries");

        this._transactionId = (transactionEntryRequest && transactionEntryRequest.transactionId)
            ? transactionEntryRequest.transactionId : "";
        this._lookupCode = (transactionEntryRequest && transactionEntryRequest.lookupCode)
            ? transactionEntryRequest.lookupCode : "";
        this._price = (transactionEntryRequest && transactionEntryRequest.price)
            ? transactionEntryRequest.price : -1;
        this._discount = (transactionEntryRequest && transactionEntryRequest.discount)
            ? transactionEntryRequest.discount : 0;
        this._quantity = (transactionEntryRequest && transactionEntryRequest.quantity)
            ? transactionEntryRequest.quantity : -1;
    }

    public get transactionId(): string { return this._transactionId; }
    public set transactionId(value: string) {
        if (this._transactionId !== value) {
            this._transactionId = value;
            this.propertyChanged(TransactionEntryFieldNames.TRANSACTION_ID);
        }
    }

    public get lookupCode(): string { return this._lookupCode; }
    public set lookupCode(value: string) {
        if (this._lookupCode !== value) {
            this._lookupCode = value;
            this.propertyChanged(TransactionEntryFieldNames.LOOKUP_CODE);
        }
    }

    public get price(): number { return this._price; }
    public set price(value: number) {
        if (this._price !== value) {
            this._price = value;
            this.propertyChanged(TransactionEntryFieldNames.PRICE);
        }
    }

    public get discount(): number { return this._discount; }
    public set discount(value: number) {
        if (this._discount !== value) {
            this._discount = value;
            this.propertyChanged(TransactionEntryFieldNames.DISCOUNT);
        }
    }

    public get quantity(): number { return this._quantity; }
    public set quantity(value: number) {
        if (this.quantity !== value) {
            this.quantity = value;
            this.propertyChanged(TransactionEntryFieldNames.QUANTITY);
        }
    }

    public toJSON(): TransactionEntry {
        return new TransactionEntry(
            super.id, this._transactionId, this._lookupCode, this._price, this._discount, this._quantity
        );
    }

    public fillFromRecord(row: any): void {
        super.fillFromRecord(row);

        this._transactionId = row[TransactionEntryFieldNames.TRANSACTION_ID.toLowerCase()];
        this._lookupCode = row[TransactionEntryFieldNames.LOOKUP_CODE.toLowerCase()];
        this._price = row[TransactionEntryFieldNames.PRICE];
        this._discount = row[TransactionEntryFieldNames.DISCOUNT];
        this._quantity = row[TransactionEntryFieldNames.QUANTITY];
    }

    public fillRecord(): any {
        let record: any = super.fillRecord();

        record[TransactionEntryFieldNames.TRANSACTION_ID] = this._transactionId;
        record[TransactionEntryFieldNames.LOOKUP_CODE] = this._lookupCode;
        record[TransactionEntryFieldNames.PRICE] = this._price;
        record[TransactionEntryFieldNames.DISCOUNT] = this._discount;
        record[TransactionEntryFieldNames.QUANTITY] = this._quantity;

        return record;
    }

    public synchronize(transactionEntryRequest: TransactionEntry): void {
        this.transactionId = (transactionEntryRequest) ? transactionEntryRequest.transactionId : "";
        this.lookupCode = (transactionEntryRequest) ? transactionEntryRequest.lookupCode : "";
        this.price = (transactionEntryRequest) ? transactionEntryRequest.price : -1;
        this.discount = (transactionEntryRequest) ? transactionEntryRequest.discount : 0;
        this.quantity = (transactionEntryRequest) ? transactionEntryRequest.quantity : -1;
    }
}