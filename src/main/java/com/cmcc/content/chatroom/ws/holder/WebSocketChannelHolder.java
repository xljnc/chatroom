package com.cmcc.content.chatroom.ws.holder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 一贫
 * @date 2020/11/5
 */
public class WebSocketChannelHolder {

    private static Map<String, ChannelHandlerContext> handlerContextMap = new ConcurrentHashMap<>();

    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();


    public static ChannelHandlerContext putChannelHandlerContext(String userId, ChannelHandlerContext ctx) {
        return handlerContextMap.put(userId, ctx);
    }

    public static ChannelHandlerContext getChannelHandlerContext(String userId) {
        return handlerContextMap.get(userId);
    }

    public static Channel putChannel(String userId, Channel channel) {
        return channelMap.put(userId, channel);
    }

    public static Channel getChannel(String userId) {
        return channelMap.get(userId);
    }
}
