package com.wt.content.chatroom.ws.handler;

import com.wt.content.chatroom.core.ChatroomConfig;
import com.wt.content.chatroom.util.JacksonUtil;
import com.wt.content.chatroom.ws.holder.WebSocketChannelHolder;
import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

/**
 * @author 一贫
 * @date 2020/11/5
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> implements ChannelOutboundHandler {

    @Autowired
    private JacksonUtil jacksonUtil;

    @Autowired
    private WebSocketChannelHolder webSocketChannelHolder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        log.debug("收到消息:{}" + text);
        WebsocketInboundMessage input = jacksonUtil.readValue(text, WebsocketInboundMessage.class);
        if (!input.validate()) {
            ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST))
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }
        //维护用户列表
        webSocketChannelHolder.bindUserToChannel(input.getUserId(), ctx.channel());
        webSocketChannelHolder.bindChannelToUser(ctx.channel(), input.getUserId());
        webSocketChannelHolder.addUserOnThisHost(input.getUserId());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.writeAndFlush(msg, promise);
        log.debug("发送消息:{}" + msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String userId = webSocketChannelHolder.getUserByChannel(ctx.channel());
        //解除用户通道绑定
        webSocketChannelHolder.unbindUserToChannel(userId);
        webSocketChannelHolder.unbindChannelToUser(ctx.channel());
        webSocketChannelHolder.removeUserOnThisHost(userId);
        log.info("链接断开：{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("链接创建：{}", ctx.channel().remoteAddress());
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
