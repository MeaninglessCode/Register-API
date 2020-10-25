const uuid = require("uuidv4");
import ProductDb from "../models/types/productDb";
import {CommandResponse, IProductDb} from "../../../types";
import ProductEntity from "../models/entities/productEntity";

export default class QueryById {
    private _id: string;
    private _productDb: IProductDb;

    constructor({ id = uuid.empty(), productDb = new ProductDb() }: any = {}) {
        this._id = id;
        this._productDb = productDb;
    }

    public get id(): string { return this._id; }
    public set id(value: string) { this._id = value; }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._productDb.get(this._id).then((entity: ProductEntity | undefined) => {
                if (entity)
                    resolve({ status: 200, message: "Product found", data: entity.toJSON() });
                else
                    reject({ status: 404, message: "Product not found", data: {} });
            },
            (reason: any) => {
                reject({ status: 500, message: reason.message, data: {} });
            });
        });
    }
}
