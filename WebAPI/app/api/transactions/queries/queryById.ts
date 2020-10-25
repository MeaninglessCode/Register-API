const async = require("async");
import { ITransactionDb, ITransactionEntryDb, CommandResponse } from "../../../types";
import TransactionDb from "../models/types/transactionDb";
import TransactionEntryDb from "../models/types/transactionEntryDb";
import TransactionEntity from "../models/entities/transactionEntity";
import TransactionEntryEntity from "../models/entities/transactionEntryEntity";

export default class QueryById {
    private _id: string;
    private _transactionDb: ITransactionDb;
    private _transactionEntryDb: ITransactionEntryDb;

    constructor({ id = "", transactionDb = new TransactionDb(), transactionEntryDb = new TransactionEntryDb()}: any) {
        this._id = id;
        this._transactionDb = this.transactionDb;
        this._transactionEntryDb = transactionEntryDb;
    }

    public get transactionId(): string { return this._id; }
    public set transactionId(value: string) { this._id = value; }

    public get transactionDb(): ITransactionDb { return this._transactionDb; }
    public set transactionDb(value: ITransactionDb) { this._transactionDb = value; }

    public get transactionEntryDb(): ITransactionEntryDb { return this._transactionEntryDb; }
    public set transactionEntryDb(value: ITransactionEntryDb) { this._transactionEntryDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._transactionDb.get(this._id).then(async (entity: TransactionEntity | undefined) => {
                if (entity) {
                    let data: any = {};

                    data.id = entity.id;
                    data.cashierId = entity.cashierId;
                    data.type = entity.type;
                    data.total = entity.total;
                    data.referenceId = entity.referenceId;
                    data.createdOn = entity.createdOn;
                    data.entries = [];

                    await this._transactionEntryDb.getEntriesByTransactionId(entity.id)
                    .then((entryEntities: TransactionEntryEntity[]) => {
                        for (let i = 0; i < entryEntities.length; i++) {
                            data.entries[i] = {};
                            data.entries[i].id = entryEntities[i].id;
                            data.entries[i].lookupCode = entryEntities[i].lookupCode;
                            data.entries[i].price = entryEntities[i].price;
                            data.entries[i].discount = entryEntities[i].discount;
                            data.entries[i].quantity = entryEntities[i].quantity;
                        }
                    },
                    (reason: any) => {
                        reject({ status: 500, message: `Invalid data: transaction id ${entity.id}`, data: [] });
                    });
                    resolve({ status: 200, message: "Success", data: data });
                }
                else {
                    reject({ status: 404, message: "Transaction not found", data: [] });
                }
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: [] });
            });
        });
    }
}