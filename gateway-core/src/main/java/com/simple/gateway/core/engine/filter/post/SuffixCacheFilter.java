package com.simple.gateway.core.engine.filter.post;

import com.simple.gateway.common.util.JsonUtil;
import com.simple.gateway.common.util.StringUtil;
import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.context.GatewayContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.core.support.RedisService;
import com.simple.gateway.core.support.ThreadService;
import com.simple.gateway.core.util.NettyHttpRequestUtil;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.model.CacheConfig;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.ProtocolType;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 后置缓存过滤器
 *
 * <p>
 *  写缓存
 * </p>
 */
@Slf4j
@Component
public class SuffixCacheFilter extends AbstractFilter {

    @Autowired
    ThreadService threadService;

    @Autowired
    RedisService redisService;

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);

        //没有配置缓存
        if (!plugin.isAllowCache()) {
            return false;
        }

        //命中缓存不需要处理
        GatewayContext gatewayContext = ChannelContext.getGatewayContext(channel);
        if (gatewayContext.isHitCache()) {
            return false;
        }

        return true;
    }

    @Override
    public void doFilter(Channel channel) {
        threadService.getCacheThreadPool().submit(() -> {
            String content = ChannelContext.getHttpResponseContent(channel);
            Route route = ChannelContext.getRoute(channel);
            Plugin plugin = ChannelContext.getPlugin(channel);

            String key = generateCacheKey(channel, route);
            int time = getCacheTime(plugin);
            redisService.writeString(key, content, time);
        });
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

    /**
     * 获取缓存时间
     */
    private int getCacheTime(Plugin plugin) {
        CacheConfig cacheConfig = plugin.fetchCacheConfig();
        int time = cacheConfig.getTime();

        return Math.min(time, CommonConstants.CACHE_MAX_TIME);
    }

}
