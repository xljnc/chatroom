package com.wt.content.chatroom.util;

import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ChatQueueSelector implements MessageQueueSelector {

    @Autowired
    private JacksonUtil jacksonUtil;

    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        if (!(arg instanceof WebsocketInboundMessage)) {
            String info = String.format("只支持WebsocketInboundMessage类型的消息,收到类型:%s", msg.getClass());
            log.error(info);
            throw new RuntimeException(info);
        }
        WebsocketInboundMessage argument = (WebsocketInboundMessage) arg;
        //按照消息类型路由到不同的queue，保证消息顺序
        //如果是群聊，则按roomId路由，如果是私聊，则用userId
        if (argument.getGroupChat()) {
            int index = argument.getRoomId().hashCode() % mqs.size();
            return mqs.get(index);
        } else {
            int index = argument.getUserId().hashCode() % mqs.size();
            return mqs.get(index);
        }
    }
}
