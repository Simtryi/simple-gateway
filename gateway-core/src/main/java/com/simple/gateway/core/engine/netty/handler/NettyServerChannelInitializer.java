package com.simple.gateway.core.engine.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //使用 HttpServerCodec 将 ByteBuf 编解码为 HttpRequest/HttpResponse
        pipeline.addLast(new HttpServerCodec(8192, 102400, 102400));

        //将 HttpMessage 和 HttpContent 聚合为 FullHttpRequest/FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));

        //路由分发
        pipeline.addLast(new DispatcherHandler());
    }

}
