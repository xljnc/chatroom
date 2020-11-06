package com.wt.content.chatroom.ws.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author 一贫
 * @date 2020/11/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsocketInboundMessage implements Serializable {

    private String topic;

    private String userId;

    private Object body;

    public boolean validate() {
        return StringUtils.hasText(topic) && StringUtils.hasText(userId);
    }
}
