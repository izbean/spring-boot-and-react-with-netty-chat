package com.izbean.springbootnettychat.repository;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRepository {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public Room getById(String id) {
        return rooms.get(id);
    }

    public void sendMessageForRoom(ChannelHandlerContext ctx, String roomId, String message) {
        Room room = getById(roomId);
        room.send(ctx, message);
    }

    public Room joinRoom(ChannelHandlerContext ctx, String roomId) {
        Room room = getById(roomId);
        room.join(ctx);
        return room;
    }

    public void leftRoom(ChannelHandlerContext ctx, String roomId) {
        Room room = getById(roomId);
        room.left(ctx);
    }

    public void changeNickname(ChannelHandlerContext ctx, String prevNickname, String roomId) {
        Room room = getById(roomId);
        room.nick(ctx, prevNickname);
    }

    public Room save(Room room) {
        return rooms.computeIfAbsent(room.getId(), k -> room);
    }

}
