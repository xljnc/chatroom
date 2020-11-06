package com.wt.content.chatroom.ws.holder;

import com.wt.content.chatroom.core.ChatroomConfig;
import com.wt.content.chatroom.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 一贫
 * @date 2020/11/5
 */
@Component
public class WebSocketChannelHolder {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ChatroomConfig chatroomConfig;

    private Map<String, ChannelHandlerContext> handlerContextMap = new ConcurrentHashMap<>();

    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private volatile String HOST_USERS_REDIS_KEY;

    private Object HOST_USERS_REDIS_KEY_LOCK = new Object();

    public ChannelHandlerContext putChannelHandlerContext(String userId, ChannelHandlerContext ctx) {
        return handlerContextMap.put(userId, ctx);
    }

    public ChannelHandlerContext getChannelHandlerContext(String userId) {
        return handlerContextMap.get(userId);
    }

    public Channel putChannel(String userId, Channel channel) {
        return channelMap.put(userId, channel);
    }

    public Channel getChannel(String userId) {
        return channelMap.get(userId);
    }

    public void clearUserOnThisHost() {
        generateHostUserRedisKey();
        redisUtil.deleteKey(HOST_USERS_REDIS_KEY);
    }

    public void addUserOnThisHost(String userId) {
        generateHostUserRedisKey();
        redisUtil.addSetValue(HOST_USERS_REDIS_KEY, userId);
    }

    public void removeUserOnThisHost(String userId) {
        generateHostUserRedisKey();
        redisUtil.removeSetValue(HOST_USERS_REDIS_KEY, userId);
    }

    public void containsUserOnThisHost(String userId) {
        generateHostUserRedisKey();
        redisUtil.containSetValue(HOST_USERS_REDIS_KEY, userId);
    }

    private void generateHostUserRedisKey(){
        if (!StringUtils.hasText(HOST_USERS_REDIS_KEY)) {
            synchronized (HOST_USERS_REDIS_KEY_LOCK) {
                if (!StringUtils.hasText(HOST_USERS_REDIS_KEY)) {
                    String hostname = chatroomConfig.getHostname();
                    HOST_USERS_REDIS_KEY = String.format("host::user::%s", hostname);
                }
            }
        }
    }

}
