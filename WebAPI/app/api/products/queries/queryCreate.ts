import { Product } from "../models/types/product";
import ProductDb from "../models/types/productDb";
import { StringExtensions } from "../../../extensions/StringExtensions";
import { CommandResponse, IProductDb } from "../../../types";
import ProductEntity from "../models/entities/productEntity";

export default class QueryCreate {
    private _productToSave: Product;
    private _productDb: IProductDb;

    constructor({ productToSave = new Product(), productDb = new ProductDb() }: any = {}) {
        this._productToSave = productToSave;
        this._productDb = productDb;
    }

    public get productToSave(): Product { return this._productToSave; }
    public set productToSave(value: Product) { this._productToSave = value; }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            if (StringExtensions.isNullOrWhitespace(this._productToSave.lookupCode))
                reject({ status: 422, message: "Missing or invalid parameter: lookupCode", data: {} });

            this._productDb.byLookupCode(this._productToSave.lookupCode).then((entity: ProductEntity | undefined) => {
                if (!entity) {
                    let newProduct = new ProductEntity(this._productToSave);

                    newProduct.save().then((value: string) => {
                        resolve({ status: 200, message: "Product created", data: newProduct.toJSON() });
                    },
                    (reason: any) => {
                        reject(reason);
                    });
                }
                else
                    reject({ status: 409, message: "lookupCode already in use", data: {} });
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: {} });
            });
        });
    }
}