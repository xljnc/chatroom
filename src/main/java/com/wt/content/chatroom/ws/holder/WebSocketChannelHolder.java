package com.wt.content.chatroom.ws.holder;

import com.wt.content.chatroom.util.RedisUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理用户ID和Netty Channel
 *
 * @author 朱群
 * @date 2020/11/5
 */
@Component
public class WebSocketChannelHolder {

    @Autowired
    private RedisUtil redisUtil;

//    @Autowired
//    private ChatroomConfig chatroomConfig;

    /**
     * key: 用户ID
     * value: netty channel
     */
    private Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    /**
     * key: netty channel
     * value: 用户ID
     */
    private Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();

//    private volatile String HOST_USERS_REDIS_KEY;
//
//    private Object HOST_USERS_REDIS_KEY_LOCK = new Object();

    /**
     * 绑定用户ID和Netty Channel
     *
     * @param userId  用户ID
     * @param channel netty channel
     * @return void
     */
    public void bindUserChannel(String userId, Channel channel) {
        bindUserToChannel(userId, channel);
        bindChannelToUser(channel, userId);
//        addUserOnThisHost(userId);
    }

    /**
     * 清理用户ID和Netty Channel绑定
     *
     * @param userId  用户ID
     * @param channel netty channel
     * @return void
     */
    public void unbindUserChannel(String userId, Channel channel) {
        unbindUserToChannel(userId);
        unbindChannelToUser(channel);
//        removeUserOnThisHost(userId);
    }

    public Channel bindUserToChannel(String userId, Channel channel) {
        return userChannelMap.put(userId, channel);
    }

    public Channel getChannelByUser(String userId) {
        return userChannelMap.get(userId);
    }

    public Channel unbindUserToChannel(String userId) {
        return userChannelMap.remove(userId);
    }

    public String bindChannelToUser(Channel channel, String userId) {
        return channelUserMap.put(channel, userId);
    }

    public String getUserByChannel(Channel channel) {
        return channelUserMap.get(channel);
    }

    public String unbindChannelToUser(Channel channel) {
        return channelUserMap.remove(channel);
    }

//    public void clearUserOnThisHost() {
//        generateHostUserRedisKey();
//        redisUtil.deleteKey(HOST_USERS_REDIS_KEY);
//    }
//
//    public void addUserOnThisHost(String userId) {
//        generateHostUserRedisKey();
//        redisUtil.addSetValue(HOST_USERS_REDIS_KEY, userId);
//    }
//
//    public void removeUserOnThisHost(String userId) {
//        generateHostUserRedisKey();
//        redisUtil.removeSetValue(HOST_USERS_REDIS_KEY, userId);
//    }
//
//    public void containsUserOnThisHost(String userId) {
//        generateHostUserRedisKey();
//        redisUtil.containSetValue(HOST_USERS_REDIS_KEY, userId);
//    }

//    private void generateHostUserRedisKey() {
//        if (!StringUtils.hasText(HOST_USERS_REDIS_KEY)) {
//            synchronized (HOST_USERS_REDIS_KEY_LOCK) {
//                if (!StringUtils.hasText(HOST_USERS_REDIS_KEY)) {
//                    String hostname = chatroomConfig.getHostname();
//                    HOST_USERS_REDIS_KEY = String.format("host::user::%s", hostname);
//                }
//            }
//        }
//    }

}
