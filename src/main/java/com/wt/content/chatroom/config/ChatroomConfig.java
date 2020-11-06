package com.wt.content.chatroom.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author 一贫
 * @date 2020/11/5
 */
@Component
@Slf4j
@Order(0)
@Getter
public class ChatroomConfig implements ApplicationRunner {

    private String hostname;

    /**
     * 获取计算机主机名
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        InetAddress ia = InetAddress.getLocalHost();
        hostname = ia.getHostName();
    }
}
