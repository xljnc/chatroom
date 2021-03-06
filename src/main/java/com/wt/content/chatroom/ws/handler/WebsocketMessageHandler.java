package com.wt.content.chatroom.ws.handler;

import com.wt.content.chatroom.service.WebsocketMessageService;
import com.wt.content.chatroom.util.JacksonUtil;
import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
import com.wt.content.chatroom.ws.protocol.WebsocketOutboundMessage;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

/**
 * 用户消息处理handler
 *
 * @author ZQ
 * @date 2020/11/5
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<WebsocketInboundMessage> implements ChannelOutboundHandler {

    @Autowired
    private WebsocketMessageService websocketMessageService;

    @Autowired
    private JacksonUtil jacksonUtil;

    /**
     * 入站消息处理
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebsocketInboundMessage msg) throws Exception {
        log.info("收到消息:{}", msg);
        websocketMessageService.handleMessage(msg);
        ctx.fireChannelRead(msg);
    }

    /**
     * 出站消息处理
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof WebsocketOutboundMessage))
            ctx.writeAndFlush(msg);
        else {
            TextWebSocketFrame wsFrame = new TextWebSocketFrame(jacksonUtil.writeValueAsString(msg));
            ctx.writeAndFlush(wsFrame, promise);
        }
        log.info("发送消息:{}" + msg);
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
