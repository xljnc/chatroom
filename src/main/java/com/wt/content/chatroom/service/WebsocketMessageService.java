package com.wt.content.chatroom.service;

import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;

/**
 * @author 一贫
 * @date 2020/11/6
 */
public interface WebsocketMessageService {

    /**
     * 实现消息处理
     *
     * @param message
     * @return void
     * @author 一贫
     * @date `2020/11/6` 19:26
     */
    void handleMessage(WebsocketInboundMessage message);
}
