package com.izbean.springbootnettychat.config.socket.payload;

public enum Command {

    INIT,

    HELLO,
    JOIN,
    HAVE,
    NICK,

    SEND,
    FROM,

    LEFT,
    BYE,
    QUIT,

    ERROR,

    CREATE_ROOM,
    ENTER_ROOM,
    EXIT_ROOM,
    RELOAD_ROOM_LIST,
    RELOAD_ROOM_ATTENDEE_COUNT

}
