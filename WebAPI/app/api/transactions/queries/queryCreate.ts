import { Transaction } from "../models/types/transaction";
import { TransactionEntry } from "../models/types/transactionEntry";
import { ITransactionDb, CommandResponse } from "../../../types";
import TransactionDb from "../models/types/transactionDb";
import TransactionEntity from "../models/entities/transactionEntity";
import TransactionEntryEntity from "../models/entities/transactionEntryEntity";

export default class QueryCreate {
    private _transactionToSave: Transaction;
    private _transactionEntriesToSave: TransactionEntry[];
    private _transactionDb: ITransactionDb;

    constructor({ transactionToSave = new Transaction(), transactionEntriesToSave = new Array<TransactionEntry>(), transactionDb = new TransactionDb() }: any = {}) {
        this._transactionToSave = transactionToSave;
        this._transactionEntriesToSave = transactionEntriesToSave;
        this._transactionDb = transactionDb;
    }

    public get transactionToSave(): Transaction { return this._transactionToSave; }
    public set transactionToSave(value: Transaction) { this._transactionToSave = value; }

    public get transactionEntriesToSave(): TransactionEntry[] { return this._transactionEntriesToSave; }
    public set transactionEntriesToSave(value: TransactionEntry[]) { this._transactionEntriesToSave = value; }

    public get transactionDb(): ITransactionDb { return this._transactionDb; }
    public set transactionDb(value: ITransactionDb) { this._transactionDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
                let newTransaction = new TransactionEntity(this._transactionToSave);

                newTransaction.save().then((value: string) => {
                    this._transactionEntriesToSave.forEach(entry => {
                        let newEntry = new TransactionEntryEntity(entry);
                        newEntry.transactionId = newTransaction.id;

                        newEntry.save().then((value: string) => {
                            return;
                        },
                        (reason: any) => {
                            reject({ status: 500, message: reason.message, data: {} });
                        });
                    });

                    resolve({ status: 200, message: "Transaction saved", data: {} });
                },
                (reason: any) => {
                    reject({ status: 500, message: reason.message, data: {} });
                });
        });
    }
}
