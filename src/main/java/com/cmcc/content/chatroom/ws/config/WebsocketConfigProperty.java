package com.cmcc.content.chatroom.ws.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 一贫
 * @date 2020/11/5
 */
@ConfigurationProperties(prefix = "websocket.netty")
@Data
@Component
public class WebsocketConfigProperty {

    private String ip;

    private int port;

    /**
     * URI路径
     */
    private String path;

    /**
     * 消息帧最大体积
     */
    private int maxFrameSize;

    /**
     * readerIdleTime读空闲超时时间设定，如果channelRead()方法超过readerIdleTime时间未被调用则会触发超时事件调用userEventTrigger()方法；
     */
    private int readerIdleTimeSeconds;

    /**
     * writerIdleTime写空闲超时时间设定，如果write()方法超过writerIdleTime时间未被调用则会触发超时事件调用userEventTrigger()方法；
     */
    private  int writerIdleTimeSeconds;

    /**
     * allIdleTime所有类型的空闲超时时间设定，包括读空闲和写空闲；
     */
    private  int allIdleTimeSeconds;

}
