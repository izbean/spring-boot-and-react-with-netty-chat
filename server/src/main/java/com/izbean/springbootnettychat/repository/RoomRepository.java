package com.izbean.springbootnettychat.repository;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRepository {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, String>

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public Room getById(String id) {
        return rooms.get(id);
    }

    public void sendMessageForRoom(ChannelHandlerContext ctx, String roomId, String message) {
        Room room = rooms.get(roomId);
        room.send(ctx, message);
    }

    public Room joinRoom(ChannelHandlerContext ctx, String roomId) {
        Room room = rooms.get(roomId);
        room.join(ctx);
        return room;
    }

    public Room save(Room room) {
        return rooms.computeIfAbsent(room.getId(), k -> room);
    }

}
