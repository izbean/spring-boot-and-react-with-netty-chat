package com.izbean.springbootnettychat.config.socket.payload;

import com.izbean.springbootnettychat.util.MapperUtils;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class Payload implements Serializable {

    private Command command;

    private String nickname;

    private Object body;

    public static Payload parse(String payload) {
        return MapperUtils.readValueOrThrow(payload, Payload.class);
    }

    @Builder
    public Payload(Command command, String nickname, Object body) {
        this.command = command;
        this.nickname = nickname;
        this.body = body;
    }

}
