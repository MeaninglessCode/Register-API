import ProductEntity from "../models/entities/productEntity";
import { CommandResponse, IProductDb } from "../../../types";
import ProductDb from "../models/types/productDb";

export default class QuerySearch {
    private _term: string;
    private _productDb: IProductDb;

    constructor({ term = "", productDb = new ProductDb() }: any = {}) {
        this._term = term;
        this._productDb = productDb;
    }

    public get term(): string { return this._term; }
    public set term(value: string) { this._term = value; }

    public get productDb(): IProductDb { return this._productDb; }
    public set productDb(value: IProductDb) { this._productDb = value; }

    public execute() {
        return new Promise<CommandResponse>((resolve, reject) => {
            this._productDb.fuzzySearch(this._term).then((entities: ProductEntity[]) => {
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
