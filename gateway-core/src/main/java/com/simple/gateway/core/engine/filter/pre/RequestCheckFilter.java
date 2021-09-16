package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.common.exception.GatewayException;
import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.attribute.AttributeKeys;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.MethodType;
import com.simple.gateway.orm.mapper.PluginMapper;
import com.simple.gateway.orm.mapper.RouteMapper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 请求校验过滤器
 */
@Slf4j
@Component
public class RequestCheckFilter extends AbstractFilter {

    @Autowired
    RouteMapper routeMapper;

    @Autowired
    PluginMapper pluginMapper;

    @Override
    public void doFilter(Channel channel) {
        FullHttpRequest httpRequest = channel.attr(AttributeKeys.HTTP_REQUEST).get();
        String uri = httpRequest.uri();
        //忽略 /favicon.ico 请求
        if(uri.equals(CommonConstants.FAVICON_ICO)){
            return;
        }

        //uri 解析
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);
        String path = queryStringDecoder.path();

        //路由校验
        Route route = routeMapper.selectByUri(path);
        if (route == null) {
            log.info("[RequestCheckFilter] uri={} not exist ", uri);
            throw new GatewayException("uri={} not exist", uri);
        } else {
            //将路由绑定到 channel 上
            ChannelContext.setRoute(channel, route);
        }

        //请求方法校验
        HttpMethod requestMethod = httpRequest.method();
        MethodType routeMethod = route.getMethod();
        if (!requestMethod.name().equals(routeMethod.name())) {
            log.info("[RequestCheckFilter] request method not match");
            throw new GatewayException("request method not match");
        }

        //绑定插件
        Plugin plugin = pluginMapper.selectByRouteId(route.getId());
        if (plugin == null) {
            //将默认插件绑定到 channel 上
            Plugin defaultPlugin = new Plugin();
            ChannelContext.setPlugin(channel, defaultPlugin);
        } else {
            //将插件绑定到 channel 上
            ChannelContext.setPlugin(channel, plugin);
        }
    }

}
