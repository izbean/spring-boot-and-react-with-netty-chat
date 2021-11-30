package com.izbean.springbootnettychat.config.socket.initialize;

import com.izbean.springbootnettychat.config.socket.codec.PayloadCodec;
import com.izbean.springbootnettychat.config.socket.handler.ChatServerHandler;
import com.izbean.springbootnettychat.config.socket.handler.WebSocketServerHandler;
import com.izbean.springbootnettychat.config.socket.payload.Payload;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {

    private final ChatServerHandler chatServerHandler;

    private final WebSocketServerHandler webSocketServerHandler;

    private final PayloadCodec payloadCodec;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG))
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(65536))
                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler("/ws", null, true))
                .addLast(webSocketServerHandler)
                .addLast(payloadCodec)
                .addLast(chatServerHandler);

    }
}
