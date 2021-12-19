package com.izbean.springbootnettychat.repository;

import com.izbean.springbootnettychat.config.socket.payload.Command;
import com.izbean.springbootnettychat.config.socket.payload.Message;
import com.izbean.springbootnettychat.config.socket.payload.Payload;
import com.izbean.springbootnettychat.dto.JoinRoomDto;
import com.izbean.springbootnettychat.dto.RoomDto;
import com.izbean.springbootnettychat.util.ChannelUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Room {

    private String id;

    private String name;

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Builder
    public Room(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public List<String> getAttendees() {
        ArrayList<String> attendees = new ArrayList<>();
        channels.forEach(channel -> attendees.add(ChannelUtils.getNickname(channel)));
        return attendees;
    }

    public void send(ChannelHandlerContext ctx, String message) {
        Payload payload = Payload.builder()
                .command(Command.FROM)
                .body(Message.from(ChannelUtils.getNickname(ctx), message))
                .build();

        channels.writeAndFlush(payload);
    }

    public void join(ChannelHandlerContext ctx) {
        Payload payload = Payload.builder()
                .command(Command.JOIN)
                .body(Message.join(ChannelUtils.getNickname(ctx)))
                .build();

        channels.add(ctx.channel());
        channels.writeAndFlush(payload);
    }

    public void exit(ChannelHandlerContext ctx) {
        Payload payload = Payload.builder()
                .command(Command.LEFT)
                .body(Message.left(ChannelUtils.getNickname(ctx)))
                .build();

        channels.remove(ctx.channel());
        channels.writeAndFlush(payload);
    }

    public static RoomDto of(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setName(room.getName());
        roomDto.setAttendeeCount(room.getChannels().size());
        return roomDto;
    }

    public static JoinRoomDto ofJoin(Room room) {
        JoinRoomDto joinRoomDto = new JoinRoomDto();
        joinRoomDto.setId(room.getId());
        joinRoomDto.setName(room.getName());
        joinRoomDto.setAttendees(room.getAttendees());
        return joinRoomDto;
    }

}
