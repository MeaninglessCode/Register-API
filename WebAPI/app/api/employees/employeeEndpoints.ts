import * as restify from "restify";
import EmployeeEndpointController from "./employeeEndpointController";
import { settings } from "../../config/config";

module.exports.endpoints = function employeeRoute(server: restify.Server) {
    let endpoint = new EmployeeEndpointController();

    server.get({ path: "/api/employee/", version: settings.version },
        endpoint.hasEmployees);
    server.get({ path: "/api/employee/:id", version: settings.version },
        endpoint.queryById);
    server.get({ path: "/api/employee/lookup/:employeeId", version: settings.version },
        endpoint.queryByEmployeeId);

    server.post({ path: "/api/employee/", version: settings.version},
        endpoint.create);
    server.post({ path: "/api/employee/login/", version: settings.version },
        endpoint.login);

    server.put({ path: "/api/employee/:id", version: settings.version },
        endpoint.update);

    server.del({ path: "/api/employee/:id", version: settings.version },
        endpoint.delete);
};