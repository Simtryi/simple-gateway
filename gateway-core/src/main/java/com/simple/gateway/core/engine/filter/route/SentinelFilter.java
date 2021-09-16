package com.simple.gateway.core.engine.filter.route;

import com.simple.gateway.core.constant.CommonConstants;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.core.support.SentinelService;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.model.SentinelConfig;
import com.simple.gateway.orm.entity.route.Route;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Sentinel熔断过滤器
 *
 * <p>
 * 1. 基于平均响应时间降级(DEGRADE_GRADE_RT)
 *  当资源的平均响应时间超过阈值(DegradeRule 中的 count)之后，资源进入准降级状态；
 *  接下来如果持续进入 5 个请求，它们的 RT 都持续超过这个阈值，那么在接下的时间窗口(DegradeRule 中的 timeWindow)之内，对这个方法的调用都会自动地返回(抛出 DegradeException)；
 *  在下一个时间窗口到来时, 会接着再放入5个请求, 再重复上面的判断。
 *
 * 2. 基于异常比例降级(DEGRADE_GRADE_EXCEPTION_RATIO)
 *  当资源的每秒异常总数占通过量的比值超过阈值(DegradeRule 中的 count)之后，资源进入降级状态；
 *  即在接下的时间窗口(DegradeRule 中的 timeWindow)之内，对这个方法的调用都会自动地返回；
 *  异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
 *
 * 3. 基于异常数降级(DEGRADE_GRADE_EXCEPTION_COUNT)
 *  当资源近 1 分钟的异常数目超过阈值之后会进行熔断。
 * </p>
 */
@Slf4j
@Component
public class SentinelFilter extends AbstractFilter {

    @Autowired
    SentinelService sentinelService;

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return  plugin.isAllowSentinel();
    }

    @Override
    public void doFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        SentinelConfig sentinelConfig = plugin.fetchSentinelConfig();
        String sentinelName = generateSentinelName(channel);

        //初始化熔断降级规则
        sentinelService.initDegradeRule(sentinelName, sentinelConfig);
    }

    private String generateSentinelName(Channel channel) {
        Route route = ChannelContext.getRoute(channel);
        return CommonConstants.SENTINEL_PREFIX + route.getUri();
    }

}
