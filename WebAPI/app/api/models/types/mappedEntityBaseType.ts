const uuid = require("uuidv4");

export class MappedEntityBaseType {
    public id: string;

    constructor(id = uuid.empty()) {
        this.id = id;
    }
}