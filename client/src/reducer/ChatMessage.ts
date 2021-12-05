export enum ChatMessageCommand {
    NICK = "NICK",
    SEND = "SEND",
    FROM = "FROM",
    HELLO = "HELLO",
    JOIN = "JOIN",
    HAVE = "HAVE",
    LEFT = "LEFT",
    ERROR = "ERROR",
}

export interface ChatMessage {
    command: ChatMessageCommand,
    nickname: string | null,
    body: string | null
}