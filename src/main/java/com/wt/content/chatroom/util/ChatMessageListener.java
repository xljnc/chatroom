package com.wt.content.chatroom.util;

import com.wt.content.chatroom.ws.holder.WebSocketChannelHolder;
import com.wt.content.chatroom.ws.protocol.WebsocketOutboundMessage;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ChatMessageListener implements MessageListenerConcurrently {

    @Autowired
    private WebSocketChannelHolder webSocketChannelHolder;

    @Autowired
    private JacksonUtil jacksonUtil;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            for (MessageExt messageExt : msgs) {
                WebsocketOutboundMessage message = jacksonUtil.readValue(messageExt.getBody(), WebsocketOutboundMessage.class);
                if (!message.validate())
                    continue;
                for (String userId : message.getUserIds()) {
                    Channel channel = webSocketChannelHolder.getChannelByUser(userId);
                    channel.writeAndFlush(message);
                }
            }
        } catch (Exception e) {
            log.error("消息消费失败,消息内容:{}", jacksonUtil.writeValueAsString(msgs), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
