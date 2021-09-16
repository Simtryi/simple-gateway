package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.common.util.JsonUtil;
import com.simple.gateway.common.util.StringUtil;
import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.context.GatewayContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.core.support.RedisService;
import com.simple.gateway.core.util.NettyHttpRequestUtil;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.ProtocolType;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 前置缓存过滤器
 *
 * <p>
 *  查缓存
 * </p>
 */
@Slf4j
@Component
public class PrefixCacheFilter extends AbstractFilter {

    @Autowired
    RedisService redisService;

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return plugin.isAllowCache();
    }

    @Override
    public void doFilter(Channel channel) {
        Route route = ChannelContext.getRoute(channel);
        String key = generateCacheKey(channel, route);

        //查询 key 对应的缓存
        String cacheString = redisService.fetchString(key);

        //没有命中缓存
        if (cacheString == null) {
            return;
        }

        //命中处理
        GatewayContext gatewayContext = ChannelContext.getGatewayContext(channel);
        gatewayContext.setHitCache(true);

        ChannelContext.setHttpResponseContent(channel, cacheString);
    }

    /**
     * 生成缓存的key
     */
    private String generateCacheKey(Channel channel, Route route) {
        FullHttpRequest httpRequest = ChannelContext.getHttpRequest(channel);
        NettyHttpRequestUtil nettyHttpRequestUtil = new NettyHttpRequestUtil(httpRequest);
        //获取请求参数
        Map<String, String> params = nettyHttpRequestUtil.getParams();
        //获取请求体
        String body = nettyHttpRequestUtil.getPostBody();

        if (route.getProtocol() == ProtocolType.DUBBO) {
            String routeInfo = StringUtil.format("{}-{}-{}-{}-{}-{}-{}-{}", route.getUri(), route.getVersion(),
                    route.getApplication(), route.getInterfaceClass(), route.getMethodName(),
                    route.getParameters(), JsonUtil.toJson(params), body);

            return CommonConstants.CACHE_PREFIX + StringUtil.md5(routeInfo);
        } else {
            String routeInfo = StringUtil.format("{}-{}-{}-{}-{}", route.getUri(), route.getVersion(),
                    route.getSource(), JsonUtil.toJson(params), body);

            return CommonConstants.CACHE_PREFIX + StringUtil.md5(routeInfo);
        }
    }

}
