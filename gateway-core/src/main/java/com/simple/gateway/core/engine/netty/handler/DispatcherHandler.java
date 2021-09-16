package com.simple.gateway.core.engine.netty.handler;

import com.simple.gateway.common.util.SpringContextUtil;
import com.simple.gateway.core.engine.context.GatewayContext;
import com.simple.gateway.core.engine.execute.GatewayExecutor;
import com.simple.gateway.core.util.NettyHttpResponseUtil;
import com.simple.gateway.core.engine.context.ChannelContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 路由分发
 */
@Slf4j
public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        Channel channel = ctx.channel();
        boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);

        ChannelContext.setHttpRequest(channel, httpRequest);
        ChannelContext.setKeepAlive(channel, keepAlive);

        //Gateway请求的上下文
        GatewayContext gatewayContext = new GatewayContext();
        ChannelContext.setGatewayContext(channel, gatewayContext);

        //GatewayExecutor 处理 channel
        GatewayExecutor gatewayExecutor = SpringContextUtil.getBean(GatewayExecutor.class);
        gatewayExecutor.execute(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[DispatcherHandler] server catch exception, {}", cause.getMessage());
        ctx.channel().writeAndFlush(NettyHttpResponseUtil.fail(cause.getMessage()))
                .addListener(ChannelFutureListener.CLOSE);
    }

}
