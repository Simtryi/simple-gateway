package com.simple.gateway.core.engine.context;

import com.simple.gateway.core.engine.attribute.AttributeKeys;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Channel 的上下文
 */
public class ChannelContext {

    public static void setRoute(Channel channel, Route route) {
        channel.attr(AttributeKeys.ROUTE).set(route);
    }

    public static Route getRoute(Channel channel) {
        return channel.attr(AttributeKeys.ROUTE).get();
    }

    public static void setPlugin(Channel channel, Plugin plugin) {
        channel.attr(AttributeKeys.PLUGIN).set(plugin);
    }

    public static Plugin getPlugin(Channel channel) {
        return channel.attr(AttributeKeys.PLUGIN).get();
    }

    public static void setGatewayContext(Channel channel, GatewayContext gatewayContext) {
        channel.attr(AttributeKeys.GATEWAY_CONTEXT).set(gatewayContext);
    }

    public static GatewayContext getGatewayContext(Channel channel) {
        return channel.attr(AttributeKeys.GATEWAY_CONTEXT).get();
    }

    public static void setHttpRequest(Channel channel, FullHttpRequest httpRequest) {
        channel.attr(AttributeKeys.HTTP_REQUEST).set(httpRequest);
    }

    public static FullHttpRequest getHttpRequest(Channel channel) {
        return channel.attr(AttributeKeys.HTTP_REQUEST).get();
    }

    public static void setHttpResponseContent(Channel channel, String httpResponseContent) {
        channel.attr(AttributeKeys.HTTP_RESPONSE_CONTENT).set(httpResponseContent);
    }

    public static String getHttpResponseContent(Channel channel) {
        return channel.attr(AttributeKeys.HTTP_RESPONSE_CONTENT).get();
    }

    public static void setKeepAlive(Channel channel, Boolean keepAlive) {
        channel.attr(AttributeKeys.KEEP_ALIVE).set(keepAlive);
    }

    public static Boolean getKeepAlive(Channel channel) {
        return channel.attr(AttributeKeys.KEEP_ALIVE).get();
    }

    public static void setException(Channel channel, Throwable throwable) {
        channel.attr(AttributeKeys.EXCEPTION).set(throwable);
    }

    public static Throwable getException(Channel channel) {
        return channel.attr(AttributeKeys.EXCEPTION).get();
    }

}
