import * as restify from "restify";

export default abstract class EndpointController {
    protected getClientIp(req: restify.Request): string {
        let ip: string = "";
        const header: (string | string[] | undefined) = req.headers["x-forwarded-for"];

        if (header && (header.length > 0))
            ip = header[0];
        else if (req.connection.remoteAddress)
            ip = req.connection.remoteAddress;

        return ip;
    }
}
