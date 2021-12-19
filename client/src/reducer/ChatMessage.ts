export enum PayloadCommand {
    INIT = "INIT",
    NICK = "NICK",
    SEND = "SEND",
    FROM = "FROM",
    HELLO = "HELLO",
    JOIN = "JOIN",
    HAVE = "HAVE",
    LEFT = "LEFT",
    ERROR = "ERROR",

    CREATE_ROOM = "CREATE_ROOM",
    ENTER_ROOM = "ENTER_ROOM",
    EXIT_ROOM = "EXIT_ROOM",
    RELOAD_ROOM_LIST = "RELOAD_ROOM_LIST",
    RELOAD_ROOM_ATTENDEE_COUNT = "RELOAD_ROOM_ATTENDEE_COUNT",
}

export interface Payload {
    command: PayloadCommand,
    body: any | null;
}

export interface Message {
    activeRoomId: string | null,
    nickname: string | null,
    message: string | null
}