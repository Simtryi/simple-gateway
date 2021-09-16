package com.simple.gateway.core.engine.netty;


import com.simple.gateway.common.exception.ServerException;
import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.netty.handler.NettyServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 服务端
 */
@Slf4j
public class NettyServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public void start() {
        this.bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss", true));
        this.workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker", true));

        try {
            long start = System.currentTimeMillis();

            ServerBootstrap b = new ServerBootstrap();
            b.group(this.bossGroup, this.workGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new NettyServerChannelInitializer())
             .option(ChannelOption.SO_BACKLOG, 1024)
             .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);

            ChannelFuture channelFuture = b.bind(CommonConstants.NETTY_SERVER_PORT).sync();
            long cost = System.currentTimeMillis() - start;

            if (channelFuture.isSuccess()) {
                log.info("[NettyServer] server successfully started in {}, cost: {}", CommonConstants.NETTY_SERVER_PORT, cost);
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("[NettyServer] server start failed, {}", e.getMessage());
            throw new ServerException("netty server start failed, {}", e.getMessage());
        }
    }

    public void shutdown() {
        try {
            if (this.bossGroup != null) {
                this.bossGroup.shutdownGracefully();
            }
            if (this.workGroup != null) {
                this.workGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error("[NettyServer] server shutdown exception, {}", e.getMessage());
        }
    }

}
