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
        channels.forEach(channel -> attendees.add(getNicknameFromChannel(channel)));
        return attendees;
    }

    public void send(ChannelHandlerContext ctx, String message) {
        Payload payload = message(Command.FROM,Message.from(getNicknameFromChannel(ctx), message));
        channels.writeAndFlush(payload);
    }

    public void join(ChannelHandlerContext ctx) {
        Payload payload = message(Command.JOIN, Message.join(getNicknameFromChannel(ctx)));
        ChannelUtils.setActiveRoomIdKey(ctx, id);
        channels.add(ctx.channel());
        channels.writeAndFlush(payload);
    }

    public void left(ChannelHandlerContext ctx) {
        Payload payload = message(Command.LEFT, Message.left(getNicknameFromChannel(ctx)));
        channels.remove(ctx.channel());
        channels.writeAndFlush(payload);
    }

    public void nick(ChannelHandlerContext ctx, String prevNickname) {
        Payload payload = message(Command.NICK, Message.nick(prevNickname, getNicknameFromChannel(ctx)));
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

    private String getNicknameFromChannel(ChannelHandlerContext ctx) {
        return getNicknameFromChannel(ctx.channel());
    }

    private String getNicknameFromChannel(Channel channel) {
        return ChannelUtils.getNickname(channel);
    }

    private Payload message(Command command, Object body) {
        return Payload.message(command, body);
    }

}
