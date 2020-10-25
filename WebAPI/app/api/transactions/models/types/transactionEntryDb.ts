import DbBase from "../../../models/db/dbBase";
import TransactionEntryEntity from "../entities/transactionEntryEntity";
import { ITransactionEntryDb } from "../../../../types";
import { TransactionEntryFieldNames } from "../constants/transactionEntryFieldNames";

export default class TransactionEntryDb extends DbBase<TransactionEntryEntity> implements ITransactionEntryDb {
    constructor() {
        super("transaction_entries");
    }

    protected createOne(row: any): TransactionEntryEntity {
        let entity = new TransactionEntryEntity();
        entity.fillFromRecord(row);
        return entity;
    }

    public getEntriesByTransactionId(transactionId: string): Promise<TransactionEntryEntity[]> {
        let query = `SELECT * FROM ${this.tableName} WHERE ` +
            `${TransactionEntryFieldNames.TRANSACTION_ID}=\${transactionId}`;

        return this.getMany(query, { "transactionId" : transactionId });
    }
}