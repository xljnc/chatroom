package com.wt.content.chatroom.ws.handler;

import com.wt.content.chatroom.util.JacksonUtil;
import com.wt.content.chatroom.ws.config.WebsocketConfigProperty;
import com.wt.content.chatroom.ws.holder.WebSocketChannelHolder;
import com.wt.content.chatroom.ws.protocol.WebsocketInboundMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户校验handler
 * 将收到的WebSocketFrame转换成WebsocketInboundMessage
 *
 * @author ZQ
 * @date 2020/11/5
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class WebsocketUserHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private JacksonUtil jacksonUtil;

    @Autowired
    private WebSocketChannelHolder webSocketChannelHolder;

    @Autowired
    private WebsocketConfigProperty websocketConfigProperty;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       log.info("与客户端建立连接，通道开启！,port:{}",websocketConfigProperty.getPort());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接！,port:{}",websocketConfigProperty.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object frame) throws Exception {
        // 判断是否Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.writeAndFlush(new PongWebSocketFrame(((WebSocketFrame)frame).content().retain()));
            return;
        }
        // 判断是否Ping消息
        if (frame instanceof PongWebSocketFrame) {
            ctx.writeAndFlush(new PongWebSocketFrame(((WebSocketFrame)frame).content().retain()));
            return;
        }
        if (frame != null && frame instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) frame;
            String uri = request.uri();
            System.out.println(uri);
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }
        }
        //只支持文本消息
        else if(frame!=null && frame instanceof TextWebSocketFrame){
            TextWebSocketFrame msg = (TextWebSocketFrame) frame;
            String text = msg.text();
            log.info("收到消息:{}", text);
//            WebsocketInboundMessage input = jacksonUtil.readValue(text, WebsocketInboundMessage.class);
//            if (!input.validate()) {
//                ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST))
//                        .addListener(ChannelFutureListener.CLOSE);
//                return;
//            }
//            //维护用户列表
//            webSocketChannelHolder.bindUserChannel(input.getUserId(), ctx.channel());
        }
        ctx.fireChannelRead(frame);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("发送错误，端口:{}",websocketConfigProperty.getPort(),cause);
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            // 判断Channel是否读空闲, 读空闲时关闭Channel
//            if (event.state().equals(IdleState.READER_IDLE)) {
//                log.info("Netty Server 通道读空闲,IP:{}", ctx.channel().remoteAddress());
//                //解除用户通道绑定
//                String userId = webSocketChannelHolder.getUserByChannel(ctx.channel());
//                webSocketChannelHolder.unbindUserChannel(userId, ctx.channel());
//                //关闭通道
//                ctx.channel().close();
//            }
//        }
//        ctx.fireUserEventTriggered(evt);
    }


}
