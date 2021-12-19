package com.izbean.springbootnettychat.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelUtils {

    private final static AttributeKey<String> nicknameAttributeKey = AttributeKey.valueOf("nickname");

    private final static AttributeKey<String> activeRoomIdKey = AttributeKey.valueOf("activeRoomId");

    public static String getNickname(ChannelHandlerContext ctx) {
        return getNickname(ctx.channel());
    }

    public static String getNickname(Channel channel) {
        return channel.attr(nicknameAttributeKey).get();
    }

    public static String getActiveRoomId(ChannelHandlerContext ctx) {
        return getActiveRoomId(ctx.channel());
    }

    public static String getActiveRoomId(Channel channel) {
        return channel.attr(activeRoomIdKey).get();
    }

}
