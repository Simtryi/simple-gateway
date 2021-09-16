package com.simple.gateway.core.engine.execute;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.simple.gateway.common.util.SpringContextUtil;
import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.error.ErrorFilter;
import com.simple.gateway.core.engine.filter.post.ResultFilter;
import com.simple.gateway.core.engine.filter.post.SuffixCacheFilter;
import com.simple.gateway.core.engine.filter.pre.*;
import com.simple.gateway.core.engine.filter.route.FlowFilter;
import com.simple.gateway.core.engine.filter.route.SentinelFilter;
import com.simple.gateway.core.support.SentinelService;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.route.Route;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 网关的执行器
 */
@Slf4j
@Component
public class GatewayExecutor extends AbstractExecutor {

    @Autowired
    RequestCheckFilter requestCheckFilter;

    @Autowired
    LoadbalancerFilter loadbalancerFilter;

    @Autowired
    WhiteListFilter whiteListFilter;

    @Autowired
    BlackListFilter blackListFilter;

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    RateLimitFilter rateLimitFilter;

    @Autowired
    PrefixCacheFilter prefixCacheFilter;

    @Autowired
    SentinelFilter sentinelFilter;

    @Autowired
    FlowFilter flowFilter;

    @Autowired
    SuffixCacheFilter suffixCacheFilter;

    @Autowired
    ResultFilter resultFilter;

    @Autowired
    ErrorFilter errorFilter;

    /**
     * 处理 channel
     */
    @Override
    protected void doExecute(Object... args) {
        if (args == null || args.length <= 0) {
            return;
        }

        Channel channel = (Channel) args[0];

        try  {
            //前置处理
            forwardAction(channel);

            //路由处理
            routeAction(channel);

            //后置处理
            postAction(channel);
        } catch (Throwable throwable) {
            //异常处理
            errorAction(channel, throwable);
        }
    }

    /**
     * 前置处理
     */
    public void forwardAction(Channel channel) {
        preRoute(channel);
    }

    /**
     * 路由处理
     */
    public void routeAction(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);

        if (plugin.isAllowSentinel()) {
            String sentinelName = generateSentinelName(channel);
            try (Entry entry = SphU.entry(sentinelName)) {
                //被Sentinel保护的逻辑
                route(channel);
            } catch (Exception e) {
                //熔断降级处理
                SentinelService sentinelService = SpringContextUtil.getBean(SentinelService.class);
                sentinelService.degradeHandle(channel);
            }
        } else {
            route(channel);
        }
    }

    /**
     * 后置处理
     */
    public void postAction(Channel channel) {
        postRoute(channel);
    }

    /**
     * 异常处理
     */
    public void errorAction(Channel channel, Throwable throwable) {
        ChannelContext.setException(channel, throwable);
        error(channel);
    }



    /**
     *  执行 "route" 之前的所有 "pre" 过滤器
     */
    private void preRoute(Channel channel) {

        //请求参数校验
        requestCheckFilter.filter(channel);

        //负载均衡
        loadbalancerFilter.filter(channel);

        //白名单
        whiteListFilter.filter(channel);

        //黑名单
        blackListFilter.filter(channel);

        //鉴权
        authTokenFilter.filter(channel);

        //限流
        rateLimitFilter.filter(channel);

        //前置缓存
        prefixCacheFilter.filter(channel);

    }

    /**
     * 执行所有的 "route" 过滤器
     */
    private void route(Channel channel) {

        //熔断降级
        sentinelFilter.filter(channel);

        //分流
        flowFilter.filter(channel);

    }

    /**
     * 执行 "route" 之后的所有 "post" 过滤器
     */
    private void postRoute(Channel channel) {

        //后置缓存
        suffixCacheFilter.filter(channel);

        //结果处理
        resultFilter.filter(channel);

    }

    /**
     * 执行所有的 "error" 过滤器
     */
    private void error(Channel channel) {

        //错误处理
        errorFilter.filter(channel);

    }

    private String generateSentinelName(Channel channel) {
        Route route = ChannelContext.getRoute(channel);
        return CommonConstants.SENTINEL_PREFIX + route.getUri();
    }

}
