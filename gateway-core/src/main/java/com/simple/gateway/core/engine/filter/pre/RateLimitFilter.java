package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.exception.GatewayException;
import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.core.support.GuavaService;
import com.simple.gateway.core.support.RedisService;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.enums.RateLimitScope;
import com.simple.gateway.orm.entity.plugin.enums.RateLimitStrategy;
import com.simple.gateway.orm.entity.plugin.model.RateLimitConfig;
import com.simple.gateway.orm.entity.route.Route;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * 限流过滤器
 */
@Slf4j
@Component
public class RateLimitFilter extends AbstractFilter {

    @Autowired
    RedisService redisService;

    @Autowired
    GuavaService guavaService;

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return plugin.isAllowRateLimit();
    }

    @Override
    public void doFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        RateLimitConfig rateLimitConfig = plugin.fetchRateLimitConfig();

        RateLimitStrategy strategy = rateLimitConfig.getStrategy();
        if (strategy == RateLimitStrategy.STANDALONE) {
            standAloneControl(channel, rateLimitConfig);
        } else if (strategy == RateLimitStrategy.CLUSTER) {
            clusterControl(channel, rateLimitConfig);
        }
    }

    /**
     * 单机限流
     */
    private void standAloneControl(Channel channel, RateLimitConfig rateLimitConfig) {
        Route route = ChannelContext.getRoute(channel);
        RateLimitScope scope = rateLimitConfig.getScope();

        if (scope == RateLimitScope.URI) {
            //单机 URI 限流
            String key = generateUriKey(route);

            final boolean ok = guavaService.qpsIncrease(key, rateLimitConfig.getQpsValue());
            if (!ok) {
                log.warn("[RateLimitFilter] 路由繁忙，已被单机限流，请稍后再试");
                throw new GatewayException(ResultCode.BAD_REQUEST, "路由繁忙，已被单机限流，请稍后再试");
            }
        } else if (scope == RateLimitScope.IP) {
            //单机 IP 限流
            String key = generateIpKey(channel);

            final boolean ok = guavaService.qpsIncrease(key, rateLimitConfig.getQpsValue());
            if (!ok) {
                log.warn("[RateLimitFilter] 路由繁忙，已被单机限流，请稍后再试");
                throw new GatewayException(ResultCode.BAD_REQUEST, "路由繁忙，已被单机限流，请稍后再试");
            }
        } else if (scope == RateLimitScope.URI_IP) {
            //单机 URI IP 限流
            String uriKey = generateUriKey(route);
            String ipKey = generateIpKey(channel);

            final boolean uriOk = guavaService.qpsIncrease(uriKey, rateLimitConfig.getQpsValue());
            final boolean ipOk = guavaService.qpsIncrease(ipKey, rateLimitConfig.getQpsValue());

            if (!uriOk || !ipOk) {
                log.warn("[RateLimitFilter] 路由繁忙，已被单机限流，请稍后再试");
                throw new GatewayException(ResultCode.BAD_REQUEST, "路由繁忙，已被单机限流，请稍后再试");
            }
        }
    }

    /**
     * 集群限流
     */
    private void clusterControl(Channel channel, RateLimitConfig rateLimitConfig) {
        Route route = ChannelContext.getRoute(channel);
        RateLimitScope scope = rateLimitConfig.getScope();

        if (scope == RateLimitScope.URI) {
            //集群 URI 限流
            String key = generateUriKey(route);

            final boolean ok = redisService.qpsIncrease(key, rateLimitConfig.getQpsValue(), 1);
            if (!ok) {
                log.warn("[RateLimitFilter] 路由繁忙，已被集群限流，请稍后再试");
                throw new GatewayException(ResultCode.BAD_REQUEST, "路由繁忙，已被集群限流，请稍后再试");
            }
        } else if (scope == RateLimitScope.IP) {
            //集群 IP 限流
            String key = generateIpKey(channel);

            final boolean ok = redisService.qpsIncrease(key, rateLimitConfig.getQpsValue(), 1);
            if (!ok) {
                log.warn("[RateLimitFilter] 路由繁忙，已被集群限流，请稍后再试");
                throw new GatewayException(ResultCode.BAD_REQUEST, "路由繁忙，已被集群限流，请稍后再试");
            }
        } else if (scope == RateLimitScope.URI_IP) {
            //集群 URI IP 限流
            String uriKey = generateUriKey(route);
            String ipKey = generateIpKey(channel);

            final boolean uriOk = redisService.qpsIncrease(uriKey, rateLimitConfig.getQpsValue(), 1);
            final boolean ipOk = redisService.qpsIncrease(ipKey, rateLimitConfig.getQpsValue(), 1);
            if (!uriOk || !ipOk) {
                log.warn("[RateLimitFilter] 路由繁忙，已被集群限流，请稍后再试");
                throw new GatewayException(ResultCode.BAD_REQUEST, "路由繁忙，已被集群限流，请稍后再试");
            }
        }
    }

    private String generateUriKey(Route route) {
        return CommonConstants.RATE_LIMIT_PREFIX + route.getUri();
    }

    private String generateIpKey(Channel channel) {
        FullHttpRequest httpRequest = ChannelContext.getHttpRequest(channel);
        Route route = ChannelContext.getRoute(channel);

        String clientIp = httpRequest.headers().get("X-Forwarded-For");
        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
            clientIp = inetSocketAddress.getAddress().getHostAddress();
        }

        return CommonConstants.RATE_LIMIT_PREFIX + route.getUri() + "_" + clientIp;
    }

}
