package com.izbean.springbootnettychat.repository;

import com.izbean.springbootnettychat.config.socket.payload.Command;
import com.izbean.springbootnettychat.config.socket.payload.Payload;
import com.izbean.springbootnettychat.dto.JoinRoomDto;
import com.izbean.springbootnettychat.dto.RoomDto;
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

    private AttributeKey<String> nicknameAttributeKey = AttributeKey.newInstance("nickname");

    @Builder
    public Room(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public List<String> getAttendees() {
        ArrayList<String> attendees = new ArrayList<>();
        channels.forEach(channel -> attendees.add(channel.attr(nicknameAttributeKey).get()));
        return attendees;
    }

    public void join(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channels.writeAndFlush(Payload.builder().command(Command.JOIN).nickname(channel.attr(nicknameAttributeKey).get()).build());
        channels.add(ctx.channel());
    }

    public void exit(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channels.remove(ctx.channel());
        channels.writeAndFlush(Payload.builder().command(Command.LEFT).nickname(channel.attr(nicknameAttributeKey).get()).build());
    }

    public static RoomDto of(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setName(room.getName());
        return roomDto;
    }

    public static JoinRoomDto ofJoin(Room room) {
        JoinRoomDto joinRoomDto = new JoinRoomDto();
        joinRoomDto.setId(room.getId());
        joinRoomDto.setId(room.getName());
        joinRoomDto.setAttendees(room.getAttendees());
        return joinRoomDto;
    }

}
