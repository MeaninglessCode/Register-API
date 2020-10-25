const uuid = require("uuidv4");
import * as moment from "moment";
import BaseEntity from "../../../models/entities/baseEntity";
import { Product } from "../types/product";
import { ProductFieldNames } from "../constants/productFieldNames";

export default class ProductEntity extends BaseEntity {
    private _lookupCode: string;
    private _name: string;
    private _description: string;
    private _price: number;
    private _inventory: number;
    private _active: boolean;
    private _createdOn: moment.Moment;
    private _lastUpdate: moment.Moment;

    constructor(productRequest?: Product) {
        super(productRequest, "products");

        this._lookupCode = (productRequest && productRequest.lookupCode)
            ? productRequest.lookupCode : "";
        this._name = (productRequest && productRequest.name)
            ? productRequest.name : "";
        this._description = (productRequest && productRequest.description)
            ? productRequest.description : "";
        this._price = (productRequest && productRequest.price)
            ? productRequest.price : -1;
        this._inventory = (productRequest && productRequest.inventory)
            ? productRequest.inventory : -1;
        this._active = (productRequest)
            ? productRequest.active : true;
        this._createdOn = (productRequest && productRequest.createdOn)
            ? productRequest.createdOn : moment();
        this._lastUpdate = (productRequest && productRequest.lastUpdate)
            ? productRequest.lastUpdate : moment();
    }

    public get lookupCode(): string { return this._lookupCode; }
    public set lookupCode(value: string) {
        if (this._lookupCode !== value) {
            this._lookupCode = value;
            this.propertyChanged(ProductFieldNames.LOOKUP_CODE);
        }
    }

    public get name(): string { return this._name; }
    public set name(value: string) {
        if (this._name !== value) {
            this._name = value;
            this.propertyChanged(ProductFieldNames.NAME);
        }
    }

    public get description(): string { return this._description; }
    public set description(value: string) {
        if (this._description !== value) {
            this._description = value;
            this.propertyChanged(ProductFieldNames.DESCRIPTION);
        }
    }

    public get price(): number { return this._price; }
    public set price(value: number) {
        if (this._price !== value) {
            this._price = value;
            this.propertyChanged(ProductFieldNames.PRICE);
        }
    }

    public get active(): boolean { return this._active; }
    public set active(value: boolean) {
        if (this._active !== value) {
            this._active = value;
            this.propertyChanged(ProductFieldNames.ACTIVE);
        }
    }

    public get inventory(): number { return this._inventory; }
    public set inventory(value: number) {
        if (this._inventory !== value) {
            this._inventory = value;
            this.propertyChanged(ProductFieldNames.INVENTORY);
        }
    }

    public get createdOn(): moment.Moment { return this._createdOn; }
    public set createdOn(value: moment.Moment) {
        if (this._createdOn !== value) {
            this._createdOn = value;
            this.propertyChanged(ProductFieldNames.CREATED_ON);
        }
    }

    public get lastUpdate(): moment.Moment { return this._lastUpdate; }
    public set lastUpdate(value: moment.Moment) {
        if (this._lastUpdate !== value) {
            this._lastUpdate = value;
            this.propertyChanged(ProductFieldNames.LAST_UPDATE);
        }
    }

    public toJSON(): Product {
        return new Product(
            super.id, this._lookupCode, this._name, this._description, this._price,
            this._inventory, this._active, this._createdOn, this._lastUpdate
        );
    }

    public fillFromRecord(row: any): void {
        super.fillFromRecord(row);

        this._lookupCode = row[ProductFieldNames.LOOKUP_CODE.toLowerCase()];
        this._name = row[ProductFieldNames.NAME];
        this._description = row[ProductFieldNames.DESCRIPTION];
        this._price = row[ProductFieldNames.PRICE];
        this._inventory = row[ProductFieldNames.INVENTORY];
        this._active = row[ProductFieldNames.ACTIVE];
        this._createdOn = moment.utc(row[ProductFieldNames.CREATED_ON.toLowerCase()], "YYYY-MM-DD HH:mm:ss");
        this._lastUpdate = moment.utc(row[ProductFieldNames.LAST_UPDATE.toLowerCase()], "YYYY-MM-DD HH:mm:ss");
    }

    protected fillRecord(): any {
        let record: any = super.fillRecord();

        record[ProductFieldNames.LOOKUP_CODE] = this._lookupCode;
        record[ProductFieldNames.NAME] = this._name;
        record[ProductFieldNames.DESCRIPTION] = this._description;
        record[ProductFieldNames.PRICE] = this._price;
        record[ProductFieldNames.INVENTORY] = this._inventory;
        record[ProductFieldNames.ACTIVE] = this._active;
        record[ProductFieldNames.CREATED_ON] = this._createdOn;
        record[ProductFieldNames.LAST_UPDATE] = this._lastUpdate;

        return record;
    }

    public synchronize(productRequest: Product): void {
        this.lookupCode = (productRequest) ? productRequest.lookupCode : "";
        this.name = (productRequest) ? productRequest.name : "";
        this.description = (productRequest) ? productRequest.description : "";
        this.price = (productRequest) ? productRequest.price : -1;
        this.inventory = (productRequest) ? productRequest.inventory : -1;
        this.active = (productRequest) ? productRequest.active : true;
        this.createdOn = (productRequest) ? productRequest.createdOn : moment();
        this.lastUpdate = (productRequest) ? productRequest.lastUpdate : moment();
    }
}