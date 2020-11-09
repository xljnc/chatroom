package com.wt.content.chatroom.service;

import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;

/**
 * 消息处理服务
 *
 * @author ZQ
 * @date 2020/11/6
 */
public interface WebsocketMessageService {

    /**
     * 消息处理
     *
     * @param message
     * @return void
     * @author ZQ
     * @date 2020/11/6 19:26
     */
    void handleMessage(WebsocketInboundMessage message);
}
