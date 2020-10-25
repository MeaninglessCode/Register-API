import * as restify from "restify";
import { settings } from "./config/config";
import { logger } from "./services/logger";

export let api = restify.createServer({
  name: settings.name
});

api.pre(restify.pre.sanitizePath());
api.use(restify.plugins.acceptParser(api.acceptable));
api.use(restify.plugins.bodyParser());
api.use(restify.plugins.queryParser());
api.use(restify.plugins.authorizationParser());
api.use(restify.plugins.fullResponse());

let productsRoute = require(`${__dirname}/api/products/productEndpoints.js`);
let employeesRoute = require(`${__dirname}/api/employees/employeeEndpoints.js`);
let transactionsRoute = require(`${__dirname}/api/transactions/transactionEndpoints.js`);

productsRoute.endpoints(api);
employeesRoute.endpoints(api);
transactionsRoute.endpoints(api);

api.listen(settings.port, function () {
  logger.info(`INFO: ${settings.name} is running at ${api.url}`);
});
