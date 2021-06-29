//package com.wt.content.chatroom.util;
//
//import com.wt.content.chatroom.config.RocketMQConfig;
//import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.client.producer.MessageQueueSelector;
//import org.apache.rocketmq.common.message.Message;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * MQ生产者
// *
// * @author ZQ
// * @date 2020/11/6
// */
//@Component
//@Slf4j
//@Order(2)
//public class RocketMQProducer implements ApplicationRunner {
//
//    @Autowired
//    private RocketMQConfig rocketMQConfig;
//
//    private DefaultMQProducer mqProducer;
//
//    @Autowired
//    private MessageQueueSelector messageQueueSelector;
//
//    @Autowired
//    private JacksonUtil jacksonUtil;
//
//    /**
//     * 发送顺序消息
//     *
//     * @param message
//     * @return boolean
//     */
//    public <T extends WebsocketInboundMessage> boolean sendOrderedMessage(T message) {
//        Message msg = new Message(rocketMQConfig.getProducerTopic(), jacksonUtil.writeValueAsBytes(message));
//        try {
//            mqProducer.send(msg, messageQueueSelector, message);
//            return true;
//        } catch (Exception e) {
//            log.error("消息发送失败，内容:{}", jacksonUtil.writeValueAsString(message), e);
//            return false;
//        }
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        mqProducer = new DefaultMQProducer(rocketMQConfig.getProducerGroup());
//        mqProducer.setNamesrvAddr(rocketMQConfig.getNamesrvAddr());
//        mqProducer.setRetryTimesWhenSendFailed(rocketMQConfig.getRetryTimesWhenSendFailed());
//        try {
//            mqProducer.start();
//            log.info("MQ生产者启动成功");
//        } catch (MQClientException e) {
//            log.error("MQ生产者启动失败", e);
//        }
//    }
//}
