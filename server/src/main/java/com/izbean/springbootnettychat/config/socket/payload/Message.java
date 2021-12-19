package com.izbean.springbootnettychat.config.socket.payload;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -4541829837849037842L;

    private String roomId;

    private String nickname;

    private String message;

    @Builder
    public Message(String roomId, String nickname, String message) {
        this.roomId = roomId;
        this.nickname = nickname;
        this.message = message;
    }

    public static Message hello(String nickname) {
        return onlyNickname(nickname);
    }

    public static Message from(String nickname, String message) {
        return nicknameAndMessage(nickname, message);
    }

    public static Message have(String nickname) {
        return onlyNickname(nickname);
    }

    public static Message join(String nickname) {
        return onlyNickname(nickname);
    }

    public static Message nick(String prevNick, String newNick) {
        return nicknameAndMessage(prevNick, newNick);
    }

    public static Message error(String message) {
        return onlyMessage(message);
    }

    public static Message left(String nickname) {
        return onlyNickname(nickname);
    }

    private static Message nicknameAndMessage(String nickname, String message) {
        return Message.builder()
                .nickname(nickname)
                .message(message)
                .build();
    }
    
    private static Message roomIdAndNickname(String roomId, String nickname) {
        return Message.builder()
                .roomId(roomId)
                .nickname(nickname)
                .build();
    }

    private static Message onlyNickname(String nickname) {
        return Message.builder()
                .nickname(nickname)
                .build();
    }

    private static Message onlyMessage(String message) {
        return Message.builder()
                .message(message)
                .build();
    }

}
