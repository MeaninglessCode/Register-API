import DbBase from "../../../models/db/dbBase";
import TransactionEntity from "../entities/transactionEntity";
import { ITransactionDb } from "../../../../types";
import { TransactionFieldNames } from "../constants/transactionFieldNames";

export default class TransactionDb extends DbBase<TransactionEntity> implements ITransactionDb {
    constructor() {
        super("transactions");
    }

    protected createOne(row: any): TransactionEntity {
        let entity = new TransactionEntity();
        entity.fillFromRecord(row);
        return entity;
    }

    public getEntriesByCashierId(cashierId: string): Promise<TransactionEntity[]> {
        let query = `SELECT * FROM ${this.tableName} WHERE ${TransactionFieldNames.CASHIER_ID}=\${cashierId}`;
        return this.getMany(query, { "cashierId": cashierId });
    }
}