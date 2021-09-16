package com.simple.gateway.core.engine.attribute;

import com.simple.gateway.core.engine.context.GatewayContext;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.route.Route;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

public class AttributeKeys {

    public static final AttributeKey<Route> ROUTE = AttributeKey.newInstance("route");

    public static final AttributeKey<Plugin> PLUGIN = AttributeKey.newInstance("plugin");

    public static final AttributeKey<GatewayContext> GATEWAY_CONTEXT = AttributeKey.newInstance("gatewayContext");

    public static final AttributeKey<FullHttpRequest> HTTP_REQUEST = AttributeKey.newInstance("httpRequest");

    public static final AttributeKey<String> HTTP_RESPONSE_CONTENT = AttributeKey.newInstance("httpResponseContent");

    public static final AttributeKey<Boolean> KEEP_ALIVE = AttributeKey.newInstance("keepAlive");

    public static final AttributeKey<Throwable> EXCEPTION = AttributeKey.newInstance("exception");

}
