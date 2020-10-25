import BaseEntity from "./api/models/entities/baseEntity";
import ProductEntity from "./api/products/models/entities/productEntity";
import EmployeeEntity from "./api/employees/models/entities/employeeEntity";
import TransactionEntity from "./api/transactions/models/entities/transactionEntity";
import TransactionEntryEntity from "./api/transactions/models/entities/transactionEntryEntity";

export interface Config {
  name: string;
  port: number;
  env: string;
  version: string;
  db: DBConfig;
}

export interface DBConfig {
  host: string,
  port: number,
  database: string,
  user: string,
  password: string
}

export interface CommandResponse {
  status: number,
  message: string,
  data: any
}

export interface IBaseDb<T extends BaseEntity> {
    all(): Promise<T[]>;
    get(id: string): Promise<T | undefined>;
    getMany(query: string, value: any): Promise<T[]>;
    getRowCount(): Promise<number>;
    exists(id: string): Promise<boolean>;
    inRange(limit: number, offset: number): Promise<T[]>;
    saveMany(toSave: T[]): Promise<null>;
    deleteMany(toDelete: T[]): Promise<null>;
    connectAndRun(context: T, action: (self: T, connection: any) => Promise<any>): Promise<any>;
  }

export interface IProductDb extends IBaseDb<ProductEntity> {
    byLookupCode(lookupCode: string): Promise<ProductEntity | undefined>;
    fuzzySearch(term: string): Promise<ProductEntity[]>;
  }

export interface IEmployeeDb extends IBaseDb<EmployeeEntity> {
    byEmployeeId(employeeId: string): Promise<EmployeeEntity | undefined>;
}

export interface ITransactionDb extends IBaseDb<TransactionEntity> {
    getEntriesByCashierId(cashierId: string): Promise<TransactionEntity[]>;
}

export interface ITransactionEntryDb extends IBaseDb<TransactionEntryEntity> {
    getEntriesByTransactionId(transactionId: string): Promise<TransactionEntryEntity[]>;
}