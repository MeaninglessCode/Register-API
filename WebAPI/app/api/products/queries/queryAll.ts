import ProductEntity from "../models/entities/productEntity";
import { CommandResponse, IProductDb } from "../../../types";
import ProductDb from "../models/types/productDb";

export default class QueryAll {
    private _productDb: IProductDb;

    constructor({ productDb = new ProductDb() }: any = {}) {
        this._productDb = productDb;
    }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._productDb.all().then((entities: ProductEntity[]) => {
                resolve({
                    status: 200, message: "Success",
                    data: entities.map((e: ProductEntity) => { return e.toJSON(); })
                });
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: {} });
            });
        });
    }
}
