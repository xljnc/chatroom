package com.wt.content.chatroom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther: 一贫
 * @date: 2019/9/10 21:03
 * @description: MQ配置类
 */
@ConfigurationProperties(prefix = "spring.rocketmq")
@Component
@Data
public class RocketMQConfig {

    private String namesrvAddr;

    private String producerGroup;

    private String consumerGroup;

    private int retryTimesWhenSendFailed;

    private String producerTopic;

    private String consumerTopic;

    private int consumeMessageBatchMaxSize;
}
