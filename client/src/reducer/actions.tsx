import { Payload, PayloadCommand } from "./ChatMessage";

export const receive = (payload: Payload) => {
    return {
        type: payload.command,
        body: payload.body
    }
}