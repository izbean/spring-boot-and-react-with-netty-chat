package com.izbean.springbootnettychat.config.socket.codec;

import com.izbean.springbootnettychat.config.socket.payload.Payload;
import com.izbean.springbootnettychat.util.MapperUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Component
@Slf4j
@ChannelHandler.Sharable
public class PayloadCodec extends MessageToMessageCodec<TextWebSocketFrame, Payload> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Payload payload, List<Object> out) {
        out.add(new TextWebSocketFrame(MapperUtils.writeValueAsStringOrThrow(payload)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame frame, List<Object> out) {
        out.add(Payload.parse(frame.content().toString(Charset.defaultCharset())));
    }

}
