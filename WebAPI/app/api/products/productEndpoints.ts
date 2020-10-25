import * as restify from "restify";
import ProductEndpointController from "./productEndpointController";
import { settings } from "../../config/config";

module.exports.endpoints = function productRoute(server: restify.Server) {
    let endpoint = new ProductEndpointController();

    server.get({ path: "/api/product/", version: settings.version },
        endpoint.queryAll);
    server.get({ path: "/api/product/:id", version: settings.version },
        endpoint.queryById);
    server.get({ path: "/api/product/lookup/:lookupCode", version: settings.version },
        endpoint.queryByLookupCode);
    server.get({ path: "/api/product/search/:term", version: settings.version },
        endpoint.querySearch);

    server.post({ path: "/api/product/", version: settings.version },
        endpoint.create);

    server.put({ path: "/api/product/:id", version: settings.version },
        endpoint.update);

    server.del({ path: "/api/product/:id", version: settings.version },
        endpoint.delete);
};
