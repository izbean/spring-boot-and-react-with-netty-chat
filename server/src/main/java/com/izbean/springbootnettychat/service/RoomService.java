package com.izbean.springbootnettychat.service;

import com.izbean.springbootnettychat.dto.JoinRoomDto;
import com.izbean.springbootnettychat.dto.RoomDto;
import com.izbean.springbootnettychat.repository.Room;
import com.izbean.springbootnettychat.repository.RoomRepository;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomDto> getRooms() {
        return roomRepository.findAll().stream()
                .map(Room::of)
                .collect(Collectors.toList());
    }

    public RoomDto getRoom(String roomId) {
        return Room.of(roomRepository.getById(roomId));
    }

    public void sendMessageForRoom(ChannelHandlerContext ctx, String roomId, String message) {
        roomRepository.sendMessageForRoom(ctx, roomId, message);
    }

    public JoinRoomDto joinRoom(ChannelHandlerContext ctx, String roomId) {
        return Room.ofJoin(roomRepository.joinRoom(ctx, roomId));
    }

    public RoomDto createRoom(String name) {
        Room newRoom = Room.builder()
                .name(name)
                .build();

        return Room.of(roomRepository.save(newRoom));
    }

}
