import { ChatMessage, ChatMessageCommand } from "./ChatMessage";

export const receive = (payload: ChatMessage) => {
    return {
        type: payload.command,
        payload
    }
}