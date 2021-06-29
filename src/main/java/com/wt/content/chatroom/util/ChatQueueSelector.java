//package com.wt.content.chatroom.util;
//
//import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.producer.MessageQueueSelector;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageQueue;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * MQ消息路由选择器
// * 控制消息发送到topic下的某个queue,保证消息局部有序
// * 规则：
// * 1. 如果是群聊，则按roomId的hashcode进行路由，保证同一个群聊的消息发送到同一个queue
// * 2. 如果是私聊，则按userId的hashcode进行路由，保证同一个用户的消息发送到同一个queue
// * 3. 消费方需要使用MessageListenerOrderly来保证顺序消费
// *
// * @author ZQ
// * @date 2020/11/6
// */
//@Component
//@Slf4j
//public class ChatQueueSelector implements MessageQueueSelector {
//
//    @Override
//    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//        if (!(arg instanceof WebsocketInboundMessage)) {
//            String info = String.format("只支持WebsocketInboundMessage类型的消息,收到类型:%s", msg.getClass());
//            log.error(info);
//            throw new RuntimeException(info);
//        }
//        WebsocketInboundMessage argument = (WebsocketInboundMessage) arg;
//        //按照消息类型路由到不同的queue，保证消息顺序
//        //如果是群聊，则按roomId路由，如果是私聊，则用userId
//        if (argument.getGroupChat()) {
//            int index = argument.getRoomId().hashCode() % mqs.size();
//            return mqs.get(index);
//        } else {
//            int index = argument.getUserId().hashCode() % mqs.size();
//            return mqs.get(index);
//        }
//    }
//}
