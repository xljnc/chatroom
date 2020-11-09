package com.wt.content.chatroom.service.impl;

import com.wt.content.chatroom.service.WebsocketMessageService;
import com.wt.content.chatroom.util.RocketMQProducer;
import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 一贫
 * @date 2020/11/6
 */
@Service
@Slf4j
public class WebsocketMessageServiceImpl implements WebsocketMessageService {

    @Value("${chatroom.topic.connect:user-connect}")
    private String userConnectTopic;

    @Autowired
    private RocketMQProducer mqProducer;

    @Override
    public void handleMessage(WebsocketInboundMessage message) {
        //用户绑定消息，直接丢弃不做处理
        if (message.getTopic().equals(userConnectTopic))
            return;
        mqProducer.sendOrderedMessage(message);
    }
}
