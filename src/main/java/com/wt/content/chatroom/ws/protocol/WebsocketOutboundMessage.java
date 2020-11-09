package com.wt.content.chatroom.ws.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author 朱群
 * @date 2020/11/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsocketOutboundMessage implements Serializable {

    private String topic;

    private Boolean groupChat;

    private List<String> userIds;

    private String roomId;

    private Object body;

    public boolean validate() {
        return StringUtils.hasText(topic) && groupChat != null && !userIds.isEmpty();
    }
}
