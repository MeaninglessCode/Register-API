const uuid = require("uuidv4");
const bcrypt = require("bcrypt");
import * as restify from "restify";
import EndpointController from "../controller/endpointController";
import { IEmployeeDb, CommandResponse } from "../../types";
import EmployeeDb from "./models/types/employeeDb";
import QueryByEmployeeId from "./queries/queryByEmployeeId";
import QueryCreate from "./queries/queryCreate";
import QueryDelete from "./queries/queryDelete";
import QueryHasEmployees from "./queries/queryHasEmployees";
import QueryUpdate from "./queries/queryUpdate";
import QueryLogin from "./queries/queryLogin";
import { Employee } from "./models/types/employee";
import EmployeeEntity from "./models/entities/employeeEntity";

export default class EmployeeEndpointController extends EndpointController {
    private static employeeDb: IEmployeeDb = new EmployeeDb();
    private static bcryptPassCount: number = 10;

    public create(req: restify.Request, res: restify.Response, next: restify.Next) {
        let newEmployee = new Employee(uuid.empty(), req.body.employeeId, req.body.first, req.body.last, req.body.role, req.body.password);

        bcrypt.hash(newEmployee.password, EmployeeEndpointController.bcryptPassCount, (err: any, hash: string) => {
            if (err !== undefined) {
                res.send(500, { "status": 500, "message" : err });
                return next();
            }
            else {
                newEmployee.password = hash;

                (new QueryCreate({ employeeToSave: newEmployee, employeeDb: EmployeeEndpointController.employeeDb })).execute()
                .then((response: CommandResponse) => {
                    res.send(response.status, response.data);
                    return next();
                },
                (reason: CommandResponse) => {
                    res.send(reason.status, reason.message);
                    return next();
                });
            }
        });
    }

    public update(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryUpdate({ employeeId: req.params.id, employeeToSave: req.body, employeeDb: EmployeeEndpointController.employeeDb }))
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
        (new QueryDelete({ employeeId: req.params.id, employeeDb: EmployeeEndpointController.employeeDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public hasEmployees(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryHasEmployees({ employeeDb: EmployeeEndpointController.employeeDb })).execute()
        .then((response: CommandResponse) => {
            res.send(response.status);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public queryById(req: restify.Request, res: restify.Response, next: restify.Next) {
        EmployeeEndpointController.employeeDb.get(req.params.id)
        .then((entity: EmployeeEntity | undefined) => {
            if (entity)
                res.send(200, entity.toJSON());
            else
                res.send(404);
        },
        (reason: CommandResponse) => {
            return next(new Error(reason.message));
        });
    }

    public queryByEmployeeId(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryByEmployeeId({ employeeId: req.params.employeeId, employeeDb: EmployeeEndpointController.employeeDb }))
        .execute().then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public login(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryLogin({ employeeId: req.body.employeeId, employeePassword: req.body.password, employeeDb: EmployeeEndpointController.employeeDb }))
        .execute().then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }
}