const async = require("async");
import { ITransactionDb, ITransactionEntryDb, CommandResponse } from "../../../types";
import TransactionDb from "../models/types/transactionDb";
import TransactionEntryDb from "../models/types/transactionEntryDb";
import TransactionEntity from "../models/entities/transactionEntity";
import TransactionEntryEntity from "../models/entities/transactionEntryEntity";

export default class QueryByCashierId {
    private _cashierId: string;
    private _transactionDb: ITransactionDb;
    private _transactionEntryDb: ITransactionEntryDb;

    constructor({ cashierId = "", transactionDb = new TransactionDb(), transactionEntryDb = new TransactionEntryDb() }: any = {}) {
        this._cashierId = cashierId;
        this._transactionDb = transactionDb;
        this._transactionEntryDb = transactionEntryDb;
    }

    public get cashierId(): string { return this._cashierId; }
    public set cashierId(value: string) { this._cashierId = value; }

    public get transactionDb(): ITransactionDb { return this._transactionDb; }
    public set transactionDb(value: ITransactionDb) { this._transactionDb = value; }

    public get transactionEntryDb(): ITransactionEntryDb { return this._transactionEntryDb; }
    public set transactionEntryDb(value: ITransactionEntryDb) { this._transactionEntryDb = value; }

    public execute(): Promise<CommandResponse> {
        return new Promise<CommandResponse>((resolve, reject) => {
            let data: any = [];

            this._transactionDb.getEntriesByCashierId(this._cashierId)
            .then(async (entities: TransactionEntity[]) => {
                for (let i = 0; i < entities.length; i++) {
                    let entityData: any = {};

                    entityData.id = entities[i].id;
                    entityData.cashierId = entities[i].cashierId;
                    entityData.type = entities[i].type;
                    entityData.total = entities[i].total;
                    entityData.referenceId = entities[i].referenceId;
                    entityData.createdOn = entities[i].createdOn;
                    entityData.entries = [];

                    await this._transactionEntryDb.getEntriesByTransactionId(entities[i].id)
                    .then((entryEntities: TransactionEntryEntity[]) => {
                        for (let j = 0; j < entryEntities.length; j++) {
                            entityData.entries[j] = {};
                            entityData.entries[j].id = entryEntities[j].id;
                            entityData.entries[j].lookupCode = entryEntities[j].lookupCode;
                            entityData.entries[j].price = entryEntities[j].price;
                            entityData.entries[j].discount = entryEntities[j].discount;
                            entityData.entries[j].quantity = entryEntities[j].quantity;
                        }
                    },
                    (reason: any) => {
                        reject({ status: 500, message: `Invalid data: transaction id ${entities[i].id}`, data: [] });
                    });
                    data[i] = entityData;
                }
                resolve({ status: 200, message: "Success", data: data });
            },
            (reason: any) => {
                reject({ status: 404, message: reason.message, data: [] });
            });
        });
    }
}