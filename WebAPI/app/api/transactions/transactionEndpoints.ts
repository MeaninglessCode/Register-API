import * as restify from "restify";
import TransactionEndpointController from "./transactionEndpointController";
import { settings } from "../../config/config";

module.exports.endpoints = function transactionRoute(server: restify.Server) {
    let endpoint = new TransactionEndpointController();

    server.get({ path: "/api/transaction/byCashierId/:cashierId", version: settings.version },
        endpoint.getTransactionsByCashierId);

    server.post({ path: "/api/transaction/", version: settings.version },
        endpoint.create);

    server.del({ path: "/api/transaction/:id", version: settings.version },
        endpoint.delete);
};