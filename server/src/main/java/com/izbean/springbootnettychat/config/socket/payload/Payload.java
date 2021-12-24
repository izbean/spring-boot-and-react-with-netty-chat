package com.izbean.springbootnettychat.config.socket.payload;

import com.fasterxml.jackson.core.type.TypeReference;
import com.izbean.springbootnettychat.dto.RoomDto;
import com.izbean.springbootnettychat.util.MapperUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mapper.Mapper;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Slf4j
public class Payload implements Serializable {

    private Command command;

    private Object body;

    public static Payload parse(String payload) {
        return MapperUtils.readValueOrThrow(payload, Payload.class);
    }

    public static Payload message(Command command, Object body) {
        return Payload.builder()
                .command(command)
                .body(body)
                .build();
    }

    @Builder
    public Payload(Command command, Object body) {
        this.command = command;
        this.body = body;
    }

    public static <T> T bodyOfClass(Payload payload, Class<T> cls) {
        String body = MapperUtils.writeValueAsStringOrThrow(payload.getBody());
        return MapperUtils.readValueOrThrow(body, cls);
    }

}
