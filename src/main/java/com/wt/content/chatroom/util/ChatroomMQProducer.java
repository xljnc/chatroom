package com.wt.content.chatroom.util;

import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

/**
 * @auther: 一贫
 * @date: 2019/9/11 11:50
 * @description:
 */
@Component
public class ChatroomMQProducer {

    @Autowired
    private MessageChannel talentEnterpriseOutput;

    /**
     * @param msg 消息对象
     * @return void
     * @description 普通消息
     * @date 15:33 2020-03-16
     */
    public <T> void sendObject(T msg) {
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
        talentEnterpriseOutput.send(message);
    }

    /**
     * @param msg 消息对象
     * @param tag 标签
     * @return void
     * @description 带标签消息
     * @date 15:32 2020-03-16
     */
    public <T> void sendObject(T msg, String tag) {
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
        talentEnterpriseOutput.send(message);
    }

    /**
     * @param msg        消息对象
     * @param tag        标签
     * @param delayLevel 延迟等级
     * @return void
     * @description 带标签延迟消息
     * @date 15:34 2020-03-16
     */
    public <T> void sendObject(T msg, String tag, Integer delayLevel) {
        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, delayLevel)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
        talentEnterpriseOutput.send(message);
    }
}
