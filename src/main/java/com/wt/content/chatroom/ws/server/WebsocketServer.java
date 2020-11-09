package com.wt.content.chatroom.ws.server;

import com.wt.content.chatroom.ws.config.WebsocketConfigProperty;
import com.wt.content.chatroom.ws.handler.WebsocketMessageHandler;
import com.wt.content.chatroom.ws.handler.WebsocketUserHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 聊天室 Netty Server
 *
 * @author ZQ
 * @date 2020/11/5
 */
@Component
@Slf4j
@Order(3)
public class WebsocketServer implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private Channel serverChannel;

    @Autowired
    private WebsocketConfigProperty websocketConfigProperty;

    @Autowired
    private WebsocketMessageHandler websocketMessageHandler;

    @Autowired
    private WebsocketUserHandler websocketUserHandler;

    /**
     * 启动 Netty Server
     *
     * @author ZQ
     * @date 2020/11/5 11:22
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
        try {
            Channel channel = serverBootstrap.bind().sync().channel();
            this.serverChannel = channel;
            log.info("Netty Server 启动成功,port={}", websocketConfigProperty.getPort());
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化 Netty Server
     * 主要关注 serverBootstrap.childHandler
     * pipeline.addLast(websocketUserHandler);
     * pipeline.addLast(websocketMessageHandler);
     * websocketUserHandler用于用户校验，并将收到的WebSocketFrame转换成WebsocketInboundMessage
     * websocketMessageHandler用户消息处理
     * 根据实际情况对这2个handler进行扩展
     * 也可以添加新的handler,Netty的handler是链式处理，由ChannelPipeline的上一个handler传递给下一个handler
     *
     * @author ZQ
     * @date
     */
    private void init() throws Exception {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(new InetSocketAddress(websocketConfigProperty.getIp(), websocketConfigProperty.getPort()));
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new ChunkedWriteHandler());
                pipeline.addLast(new HttpObjectAggregator(websocketConfigProperty.getMaxFrameSize()));
                pipeline.addLast(new IdleStateHandler(websocketConfigProperty.getReaderIdleTimeSeconds(), websocketConfigProperty.getWriterIdleTimeSeconds(), websocketConfigProperty.getAllIdleTimeSeconds()));
                pipeline.addLast(new WebSocketServerCompressionHandler());
                pipeline.addLast(new WebSocketServerProtocolHandler(websocketConfigProperty.getPath(), null, true, websocketConfigProperty.getMaxFrameSize()));
                pipeline.addLast(websocketUserHandler);
                pipeline.addLast(websocketMessageHandler);
            }
        });
    }

    /**
     * 停止 Netty Server
     *
     * @author ZQ
     * @date 2020/11/5 11:22
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("开始停止 Netty Server.");
        if (this.serverChannel != null) {
            this.serverChannel.close();
        }
        log.info("Netty Server停止完成.");
    }
}
