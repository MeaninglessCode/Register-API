import * as restify from "restify";
const uuid = require("uuidv4");
import EndpointController from "../controller/endpointController";
import { ITransactionDb, CommandResponse, ITransactionEntryDb } from "../../types";
import TransactionDb from "./models/types/transactionDb";
import QueryCreate from "./queries/queryCreate";
import { Transaction } from "./models/types/transaction";
import { TransactionEntry } from "./models/types/transactionEntry";
import QueryDelete from "./queries/queryDelete";
import QueryByCashierId from "./queries/queryByCashierId";
import TransactionEntryDb from "./models/types/transactionEntryDb";

export default class TransactionEndpointController extends EndpointController {
    private static transactionDb: ITransactionDb = new TransactionDb();
    private static transactionEntryDb: ITransactionEntryDb = new TransactionEntryDb();

    public create(req: restify.Request, res: restify.Response, next: restify.Next) {
        let newTransaction = new Transaction(uuid.empty(), req.body.cashierId, req.body.type, req.body.total,
            req.body.referenceId);
        let newTransactionEntires = new Array<TransactionEntry>();

        for (let i = 0; i < req.body.entries.length; i++) {
            newTransactionEntires.push(new TransactionEntry(uuid.empty(), uuid.empty(), req.body.entries[i].lookupCode,
                req.body.entries[i].price, req.body.entries[i].discount, req.body.entries[i].quantity));
        }

        (new QueryCreate({ transactionToSave: newTransaction, transactionEntriesToSave: newTransactionEntires, transactionDb: TransactionEndpointController.transactionDb }))
        .execute().then((response: CommandResponse) => {
            res.send(response.status);
            return next();
        },
        (reason: CommandResponse) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public delete(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryDelete({ transactionId: req.params.id, transactionDb: TransactionEndpointController.transactionDb }))
        .execute().then((response: CommandResponse) => {
            res.send(response.status, response.message);
            return next();
        },
        (reason: any) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }

    public getTransactionsByCashierId(req: restify.Request, res: restify.Response, next: restify.Next) {
        (new QueryByCashierId({ cashierId: req.params.cashierId, transactionDb: TransactionEndpointController.transactionDb, transactionEntryDb: TransactionEndpointController.transactionEntryDb }))
        .execute().then((response: CommandResponse) => {
            res.send(response.status, response.data);
            return next();
        },
        (reason: any) => {
            res.send(reason.status, reason.message);
            return next();
        });
    }
}