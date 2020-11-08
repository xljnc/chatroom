package com.wt.content.chatroom.util;

import com.wt.content.chatroom.config.RocketMQConfig;
import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RocketMQProducer {

    @Autowired
    private RocketMQConfig rocketMQConfig;

    private DefaultMQProducer mqProducer;

    @Autowired
    private MessageQueueSelector messageQueueSelector;

    @Autowired
    private JacksonUtil jacksonUtil;

    @PostConstruct
    public void init() {
        mqProducer = new DefaultMQProducer(rocketMQConfig.getProducerGroup());
        mqProducer.setNamesrvAddr(rocketMQConfig.getNamesrvAddr());
        mqProducer.setRetryTimesWhenSendFailed(rocketMQConfig.getRetryTimesWhenSendFailed());
        try {
            mqProducer.start();
            log.info("MQ生产者启动成功");
        } catch (MQClientException e) {
            log.error("MQ生产者启动失败", e);
        }
    }

    public <T extends WebsocketInboundMessage> boolean sendOrderedMessage(T message) {
        Message msg = new Message(rocketMQConfig.getProducerTopic(), jacksonUtil.writeValueAsBytes(message));
        try {
            mqProducer.send(msg, messageQueueSelector, message);
            return true;
        } catch (Exception e) {
            log.error("消息发送失败，内容:{}", jacksonUtil.writeValueAsString(message), e);
            return false;
        }
    }
}
