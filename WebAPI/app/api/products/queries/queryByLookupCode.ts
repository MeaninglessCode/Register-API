import ProductEntity from "../models/entities/productEntity";
import { CommandResponse, IProductDb } from "../../../types";
import ProductDb from "../models/types/productDb";

export default class QueryByLookupCode {
    private _lookupCode: string;
    private _productDb: IProductDb;

    constructor({ lookupCode = "", productDb = new ProductDb() }: any = {}) {
        this._lookupCode = lookupCode;
        this._productDb = productDb;
    }

    public get lookupCode(): string { return this._lookupCode; }
    public set lookupCode(value: string) { this._lookupCode = value; }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._productDb.byLookupCode(this._lookupCode).then((entity: ProductEntity | undefined) => {
                if (entity)
                    resolve({ status: 200, message: "Success", data: entity.toJSON() });
                else
                    reject({ status: 404, message: "Product not found", data: {} });
            },
            (reason: any) => {
                reject({ status: 400, message: reason.message, data: {} });
            });
        });
    }
}
