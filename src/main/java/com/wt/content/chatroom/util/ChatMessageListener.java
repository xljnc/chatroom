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

/**
 * 消息处理服务
 *
 * @author ZQ
 * @date 2020/11/6
 */
@Component
@Slf4j
public class ChatMessageListener implements MessageListenerConcurrently {

    @Autowired
    private WebSocketChannelHolder webSocketChannelHolder;

    @Autowired
    private JacksonUtil jacksonUtil;

    /**
     * 处理收到的MQ中的WebsocketOutboundMessage
     * 广播模式
     * 从WebSocketChannelHolder获取当前服务器上的用户链接，发送消息
     *
     * @param msgs    消费的消息列表，最大长度由配置文件中的consumeMessageBatchMaxSize控制
     * @param context
     * @return org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            for (MessageExt messageExt : msgs) {
                WebsocketOutboundMessage message = jacksonUtil.readValue(messageExt.getBody(), WebsocketOutboundMessage.class);
                if (!message.validate())
                    continue;
                for (String userId : message.getUserIds()) {
                    Channel channel = webSocketChannelHolder.getChannelByUser(userId);
                    //用户可能不在当前服务器上
                    if (channel != null)
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
