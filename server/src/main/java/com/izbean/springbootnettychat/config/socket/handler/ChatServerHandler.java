package com.izbean.springbootnettychat.config.socket.handler;

import com.izbean.springbootnettychat.config.socket.payload.Command;
import com.izbean.springbootnettychat.config.socket.payload.Payload;
import com.izbean.springbootnettychat.config.socket.provider.NicknameProvider;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<Payload> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final AttributeKey<String> nickAttr = AttributeKey.newInstance("nickname");

    private final static NicknameProvider nicknameProvider = new NicknameProvider();

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        channels.writeAndFlush(message(Command.LEFT, nickname(ctx)));
        nicknameProvider.release(nickname(ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Payload payload) throws Exception {
        log.debug("channelRead0 Payload: {}", payload);

        switch (payload.getCommand()) {
            case HELLO:
                hello(ctx.channel());
                break;
            case SEND:
                channels.writeAndFlush(message(Command.FROM, nickname(ctx), payload.getBody()));
                break;
            case QUIT:
                ctx.writeAndFlush(message(Command.BYE, payload.getNickname()));
                ctx.close();
                break;
            case NICK:
                changeNickname(ctx, payload);
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
            ctx.writeAndFlush(message(Command.ERROR, null, t.getMessage()))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void hello(Channel channel) {
        if (nickname(channel) != null) return;
        String nickname = nicknameProvider.reserve();
        if (nickname == null) {
            channel.writeAndFlush(message(Command.ERROR, "Sorry, no more names for you"))
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            bindNickname(channel, nickname);
            channels.forEach(c -> channel.write(message(Command.HAVE, nickname(c))));
            channels.writeAndFlush(message(Command.JOIN, nickname));
            channels.add(channel);
            channel.writeAndFlush(message(Command.HELLO, nickname));
        }
    }

    private Payload message(Command command, String nickname) {
        return message(command, nickname, null);
    }

    private Payload message(Command command, String nickname, String body) {
        return Payload.builder()
                .command(command)
                .nickname(nickname)
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
        String newNick = payload.getBody();
        String prev = nickname(ctx);
        if (!newNick.equals(prev) && nicknameProvider.available(newNick)) {
            nicknameProvider.release(prev).reserve(newNick);
            bindNickname(ctx.channel(), newNick);
            channels.writeAndFlush(message(Command.NICK, prev, newNick));
        } else {
            ctx.writeAndFlush(message(Command.ERROR, null, "couldn't change"));
        }
    }

}
