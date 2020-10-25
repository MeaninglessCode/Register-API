const uuid = require("uuidv4");
import { ITransactionDb, CommandResponse } from "../../../types";
import TransactionDb from "../models/types/transactionDb";
import TransactionEntity from "../models/entities/transactionEntity";

export default class QueryDelete {
    private _transactionId: string;
    private _transactionDb: ITransactionDb;

    constructor({ transactionId = uuid.empty(), transactionDb = new TransactionDb() }: any = {}) {
        this._transactionId = transactionId;
        this._transactionDb = transactionDb;
    }

    public get transactionId(): string { return this._transactionId; }
    public set transactionId(value: string) { this._transactionId = value; }

    public get transactionDb(): ITransactionDb { return this._transactionDb; }
    public set transactionDb(value: ITransactionDb) { this._transactionDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._transactionDb.get(this._transactionId).then((entity: TransactionEntity | undefined) => {
                if (entity) {
                    entity.delete().then(() => {
                        resolve({ status: 200, message: "Success", data: {} });
                    },
                    (reason: any) => {
                        reject({ status: 500, message: reason.message, data: {} });
                    });
                }
                else
                    reject({ reason: 404, message: "Transaction not found", data: {} });
            });
        });
    }
}
