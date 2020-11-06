//package com.wt.content.chatroom.core;
//
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.Input;
//import org.springframework.cloud.stream.annotation.Output;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.SubscribableChannel;
//
///**
// * @auther: 一贫
// * @date: 2019/9/10 21:03
// * @description: MQ配置类
// */
//@EnableBinding({RocketMQConfig.RocketMQSource.class, RocketMQConfig.RocketMQSink.class})
//public class RocketMQConfig {
//
//    public static final String TALENT_ENTERPRISE_OUTPUT = "talentEnterpriseOutput";
//
//    public interface RocketMQSource {
//        @Output(TALENT_ENTERPRISE_OUTPUT)
//        MessageChannel talentEnterpriseOutput();
//    }
//
//    public static final String UPDATE_ENTERPRISE_INPUT = "updateEnterpriseInput";
//
//    public static final String UPDATE_TALENT_ENTERPRISE_INPUT = "updateTalentEnterpriseInput";
//
//    public static final String ENTERPRISE_STATISTIC_INPUT = "enterpriseStatisticInput";
//
//    public interface RocketMQSink {
//        @Input(UPDATE_ENTERPRISE_INPUT)
//        SubscribableChannel updateEnterpriseInput();
//
//        @Input(UPDATE_TALENT_ENTERPRISE_INPUT)
//        SubscribableChannel updateTalentEnterpriseInput();
//
//        @Input(ENTERPRISE_STATISTIC_INPUT)
//        SubscribableChannel enterpriseStatisticInput();
//    }
//
//
//}
