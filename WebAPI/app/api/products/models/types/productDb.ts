import * as restify from "restify";
import DbBase from "../../../models/db/dbBase";
import ProductEntity from "../entities/productEntity";
import db = require("../../../models/db/dbConnection");
import { IProductDb } from "../../../../types";
import { StringExtensions } from "../../../../extensions/StringExtensions";
import { ProductFieldNames } from "../constants/productFieldNames";

export default class ProductDb extends DbBase<ProductEntity> implements IProductDb {
    constructor() {
        super("products");
    }

    protected createOne(row: any): ProductEntity {
        let entity = new ProductEntity();
        entity.fillFromRecord(row);
        return entity;
    }

    public byLookupCode(lookupCode: string): Promise<ProductEntity | undefined> {
        let query = `SELECT * FROM ${this.tableName} WHERE ${ProductFieldNames.LOOKUP_CODE}=\${lookupCode}`;
        return this.firstOrDefaultWhere(query, { "lookupCode" : lookupCode });
    }

    public fuzzySearch(term: string): Promise<ProductEntity[]> {
        let query = `SELECT * FROM ${this.tableName} WHERE ${ProductFieldNames.LOOKUP_CODE} LIKE '%' || \${term} || '%'`
            + `OR ${ProductFieldNames.NAME} LIKE '%' ||\${term} || '%' OR ${ProductFieldNames.DESCRIPTION} LIKE '%' || `
            + `\${term} || '%'`;
        return this.getMany(query, { "term" : term });
    }
}