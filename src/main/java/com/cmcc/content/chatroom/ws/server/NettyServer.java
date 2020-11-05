package com.cmcc.content.chatroom.ws.server;

import com.cmcc.content.chatroom.ws.config.NettyConfigProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
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
 * @author 一贫
 * @date 2020/11/5
 */
@Component
@Slf4j
@Order(1)
public class NettyServer implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private Channel serverChannel;

    @Autowired
    private NettyConfigProperty nettyConfigProperty;

    /**
     * 启动 Netty Server
     *
     * @author 一贫
     * @date 2020/11/5 11:22
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
        try {
            Channel channel = serverBootstrap.bind().sync().channel();
            this.serverChannel = channel;
            log.info("Netty Server 启动成功,ip={},port={}", nettyConfigProperty.getIp(), nettyConfigProperty.getPort());
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 初始化 Netty Server
     *
     * @author 一贫
     * @date
     */
    private void init() throws Exception {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(new InetSocketAddress(nettyConfigProperty.getIp(), nettyConfigProperty.getPort()));
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new ChunkedWriteHandler());
                pipeline.addLast(new HttpObjectAggregator(nettyConfigProperty.getMaxFrameSize()));
                pipeline.addLast(new IdleStateHandler(nettyConfigProperty.getReaderIdleTimeSeconds(), nettyConfigProperty.getWriterIdleTimeSeconds(), nettyConfigProperty.getAllIdleTimeSeconds()));
                pipeline.addLast(new WebSocketServerCompressionHandler());
                pipeline.addLast(new WebSocketServerProtocolHandler(nettyConfigProperty.getPath(), null, true, nettyConfigProperty.getMaxFrameSize()));
            }
        });


    }

    /**
     * 停止 Netty Server
     *
     * @author 一贫
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
