const uuid = require("uuidv4");
import { Product } from "../models/types/product";
import {CommandResponse, IProductDb} from "../../../types";
import ProductEntity from "../models/entities/productEntity";
import { StringExtensions } from "../../../extensions/StringExtensions";
import ProductDb from "../models/types/productDb";

export default class QueryUpdate {
    private _productId: string;
    private _productToSave: Product;
    private _productDb: IProductDb;

    constructor({ productId = uuid.empty(), productToSave = new Product(), productDb = new ProductDb() }: any = {}) {
        this._productId = productId;
        this._productToSave = productToSave;
        this._productDb = productDb;
    }

    public get productId(): string { return this._productId; }
    public set productId(value: string) { this._productId = value; }

    public get productToSave(): Product { return this._productToSave; }
    public set productToSave(value: Product) { this._productToSave = value; }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            if (StringExtensions.isNullOrWhitespace(this._productToSave.lookupCode))
                reject({ status: 422, message: "Missing or invalid parameter: lookupCode", data: {} });

            this._productDb.get(this._productId).then((entity: ProductEntity | undefined) => {
                if (entity) {
                    entity.synchronize(this._productToSave);

                    entity.save().then((value: string) => {
                        resolve({ status: 200, message: "Success", data: entity.toJSON() });
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
