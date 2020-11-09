package com.wt.content.chatroom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther: ZQ
 * @date: 2019/9/10 21:03
 * @description: MQ配置类
 */
@ConfigurationProperties(prefix = "spring.rocketmq")
@Component
@Data
public class RocketMQConfig {

    /**
     * namesever地址，多个地址使用;分割
     */
    private String namesrvAddr;

    /**
     * 生产者组
     */
    private String producerGroup;

    /**
     * 消费者组
     */
    private String consumerGroup;

    /**
     * 同步模式下
     * 发送失败重试次数
     * 默认2次
     */
    private int retryTimesWhenSendFailed;

    /**
     * 生产者topic
     */
    private String producerTopic;

    /**
     * 消费者topic
     */
    private String consumerTopic;

    /**
     * 消费者注册的回调listener一次处理的消息数
     * 默认是1
     */
    private int consumeMessageBatchMaxSize;
}
