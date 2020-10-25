const uuid = require("uuidv4");
import { CommandResponse, IProductDb } from "../../../types";
import ProductEntity from "../models/entities/productEntity";
import ProductDb from "../models/types/productDb";

export default class QueryDelete {
    private _productId: string;
    private _productDb: IProductDb;

    constructor({ productId = uuid.empty(), productDb = new ProductDb() }: any = {}) {
        this._productId = productId;
        this._productDb = productDb;
    }

    public get productId(): string { return this._productId; }
    public set productId(value: string) { this._productId = value; }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._productDb.get(this._productId).then((entity: ProductEntity | undefined) => {
                if (entity) {
                    entity.delete().then(() => {
                        resolve({ status: 200, message: "Success", data: {} });
                    },
                    (reason: any) => {
                        reject({ status: 500, message: reason.message, data: {} });
                    });
                }
                else
                    reject({ status: 404, message: "Product not found", data: {} });
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: {} });
            });
        });
    }
}
