package com.izbean.springbootnettychat.config.socket.handler;

import com.izbean.springbootnettychat.config.socket.payload.Command;
import com.izbean.springbootnettychat.config.socket.payload.Message;
import com.izbean.springbootnettychat.config.socket.payload.Payload;
import com.izbean.springbootnettychat.config.socket.provider.NicknameProvider;
import com.izbean.springbootnettychat.dto.JoinRoomDto;
import com.izbean.springbootnettychat.dto.RoomDto;
import com.izbean.springbootnettychat.service.RoomService;
import com.izbean.springbootnettychat.util.MapperUtils;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<Payload> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final AttributeKey<String> nickAttr = AttributeKey.newInstance("nickname");

    private final static NicknameProvider nicknameProvider = new NicknameProvider();

    private final RoomService roomService;

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        channels.writeAndFlush(message(Command.LEFT, Message.left(nickname(ctx))));
        nicknameProvider.release(nickname(ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Payload payload) throws Exception {
        log.debug("channelRead0 Payload: {}", payload);

        switch (payload.getCommand()) {
            case INIT:
                ctx.writeAndFlush(message(Command.INIT, roomService.getRooms()));
                break;
            case HELLO:
                hello(ctx.channel());
                RoomDto room = roomService.createRoom(nickname(ctx) + "의 채팅방");
                channels.writeAndFlush(message(Command.CREATE_ROOM, room));
                ctx.channel().writeAndFlush(message(Command.ENTER_ROOM, roomService.joinRoom(ctx, room.getId())));
                channels.writeAndFlush(message(Command.RELOAD_ROOM_ATTENDEE_COUNT, roomService.getRoom(room.getId())));
                break;
            case SEND:
                Message send = Payload.bodyOfClass(payload, Message.class);
                roomService.sendMessageForRoom(ctx, send.getRoomId(), send.getMessage());
                break;
            case QUIT:
                ctx.writeAndFlush(message(Command.BYE, Message.left(nickname(ctx))));
                ctx.close();
                break;
            case NICK:
                changeNickname(ctx, payload);
                break;
            case CREATE_ROOM:
                channels.writeAndFlush(message(Command.CREATE_ROOM, roomService.createRoom(Payload.bodyOfClass(payload, RoomDto.class).getName())));
                break;
            case RELOAD_ROOM_LIST:
                ctx.writeAndFlush(message(Command.RELOAD_ROOM_LIST, roomService.getRooms()));
                break;
            case ENTER_ROOM:
                JoinRoomDto joinRoomDto = roomService.joinRoom(ctx, Payload.bodyOfClass(payload, RoomDto.class).getId());
                ctx.channel().writeAndFlush(message(Command.RELOAD_ROOM_ATTENDEE_COUNT, roomService.getRoom(joinRoomDto.getId())));
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported command. [%s]", payload.getCommand()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        t.printStackTrace();
        if (!ctx.channel().isActive()) {
            ctx.writeAndFlush(message(Command.ERROR, Message.error(t.getMessage())))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void hello(Channel channel) {
        if (nickname(channel) != null) return;
        String nickname = nicknameProvider.reserve();
        if (nickname == null) {
            channel.writeAndFlush(message(Command.ERROR, Message.error("Sorry, no more names for you")))
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            bindNickname(channel, nickname);
            channel.writeAndFlush(message(Command.HELLO, Message.hello(nickname)));
            channels.add(channel);
        }
    }

    private Payload message(Command command, Object body) {
        return Payload.builder()
                .command(command)
                .body(body)
                .build();
    }

    private void bindNickname(Channel c, String nickname) {
        c.attr(nickAttr).set(nickname);
    }

    private String nickname(ChannelHandlerContext ctx) {
        return nickname(ctx.channel());
    }

    private String nickname(Channel channel) {
        return channel.attr(nickAttr).get();
    }

    private void changeNickname(ChannelHandlerContext ctx, Payload payload) {
        String newNick = ((Message) payload.getBody()).getMessage();
        String prev = nickname(ctx);
        if (!newNick.equals(prev) && nicknameProvider.available(newNick)) {
            nicknameProvider.release(prev).reserve(newNick);
            bindNickname(ctx.channel(), newNick);
            channels.writeAndFlush(message(Command.NICK, Message.nick(prev, newNick)));
        } else {
            ctx.writeAndFlush(message(Command.ERROR, Message.error("couldn't change.")));
        }
    }

}
