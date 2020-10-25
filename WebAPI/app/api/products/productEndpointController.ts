const uuid = require("uuidv4");
import * as restify from "restify";
import EndpointController from "../controller/endpointController";
import { IProductDb, CommandResponse } from "../../types";
import ProductDb from "./models/types/productDb";
import QueryById from "./queries/queryById";
import QueryByLookupCode from "./queries/queryByLookupCode";
import QueryCreate from "./queries/queryCreate";
import QueryDelete from "./queries/queryDelete";
import QueryAll from "./queries/queryAll";
import QueryUpdate from "./queries/queryUpdate";
import ProductEntity from "./models/entities/productEntity";
import { Product } from "./models/types/product";
import QuerySearch from "./queries/querySearch";

export default class ProductEndpointController extends EndpointController {
    private static productDb: IProductDb = new ProductDb();

    public create(req: restify.Request, res: restify.Response, next: restify.Next) {
        let newProduct = new Product(uuid.empty(), req.body.lookupCode, req.body.name,
            req.body.description, req.body.price, req.body.inventory);

        (new QueryCreate({ productToSave: newProduct, productDb: ProductEndpointController.productDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public update(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryUpdate({ productId: req.params.id, productToSave: req.body, productDb: ProductEndpointController.productDb }))
        .execute().then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public delete(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryDelete({ productId: req.params.id, productDb: ProductEndpointController.productDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public queryAll(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryAll({ productDb: ProductEndpointController.productDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.data);
            return next();
        });
    }

    public queryById(req: restify.Request, res: restify.Response, next: restify.Next) {
        ProductEndpointController.productDb.get(req.params.id)
        .then((entity: ProductEntity | undefined) => {
            if (entity)
                res.send(200, entity.toJSON());
            else
                res.send(404);
        },
        (reason: any) => {
            return next(new Error(reason.message));
        });
    }

    public queryByLookupCode(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryByLookupCode({ lookupCode: req.params.lookupCode, productDb: ProductEndpointController.productDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public querySearch(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QuerySearch({ term: req.params.term, productDb: ProductEndpointController.productDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }
}