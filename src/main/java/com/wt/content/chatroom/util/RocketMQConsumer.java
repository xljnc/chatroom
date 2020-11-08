package com.wt.content.chatroom.util;

import com.wt.content.chatroom.config.RocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(1)
public class RocketMQConsumer implements ApplicationRunner {

    @Autowired
    private RocketMQConfig rocketMQConfig;

    private DefaultMQPushConsumer mqConsumer;

    @Autowired
    private ChatMessageListener chatMessageListener;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqConsumer = new DefaultMQPushConsumer(rocketMQConfig.getConsumerGroup());
        mqConsumer.setNamesrvAddr(rocketMQConfig.getNamesrvAddr());
        mqConsumer.setMessageModel(MessageModel.BROADCASTING);
        // 批量消费,每次拉取10条
        mqConsumer.setConsumeMessageBatchMaxSize(rocketMQConfig.getConsumeMessageBatchMaxSize());
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        mqConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqConsumer.registerMessageListener(chatMessageListener);
        try {
            mqConsumer.subscribe(rocketMQConfig.getConsumerTopic(), "*");
            mqConsumer.start();
            log.info("MQ消费者启动成功");
        } catch (MQClientException e) {
            log.error("MQ消费者启动失败", e);
        }
    }
}
