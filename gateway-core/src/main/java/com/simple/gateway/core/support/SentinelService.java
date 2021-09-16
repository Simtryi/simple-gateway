package com.simple.gateway.core.support;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.util.NettyHttpResponseUtil;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.enums.SentinelRule;
import com.simple.gateway.orm.entity.plugin.model.SentinelConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SentinelService {

    private static List<DegradeRule> rules = new ArrayList<>();
    private static Map<String, DegradeRule> ruleMap = new ConcurrentHashMap<>();

    /**
     * 初始化熔断降级规则
     *
     * @param name            被保护的资源名称
     * @param sentinelConfig  熔断降级配置
     */
    public void initDegradeRule(String name, SentinelConfig sentinelConfig) {
        DegradeRule rule = ruleMap.get(name);
        if (rule == null) {
            rule = new DegradeRule();
            rule.setResource(name);
            rules.add(rule);
        }

        if (sentinelConfig.getRule() == SentinelRule.RT) {

            //设置降级规则为RT
            rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
            //设置平均响应时间阈值(ms)
            rule.setCount(sentinelConfig.getRtValue());
            //设置时间窗口(s)
            rule.setTimeWindow(sentinelConfig.getTimeWindow());

        } else if (sentinelConfig.getRule() == SentinelRule.EXCEPTION_RATIO) {

            //设置降级规则为EXCEPTION_RATIO
            rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
            //设置异常比例阈值[0.0, 1.0]
            rule.setCount(sentinelConfig.getExceptionRatioValue());
            //设置时间窗口(s)
            rule.setTimeWindow(sentinelConfig.getTimeWindow());

        } else if (sentinelConfig.getRule() == SentinelRule.EXCEPTION_COUNT) {

            //设置降级规则为EXCEPTION_COUNT
            rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
            //设置异常数阈值
            rule.setCount(sentinelConfig.getExceptionCountValue());
            //设置时间窗口(s)，这里要求的时间窗口最小值是1m
            rule.setTimeWindow(80);

        }

        DegradeRuleManager.loadRules(rules);
    }

    /**
     * 熔断降级处理
     */
    public void degradeHandle(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        SentinelConfig sentinelConfig = plugin.fetchSentinelConfig();
        FullHttpResponse httpResponse = NettyHttpResponseUtil.fail(sentinelConfig.getMessage());

        Boolean keepAlive = ChannelContext.getKeepAlive(channel);
        if (keepAlive) {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        } else {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }

        ChannelFuture future = channel.writeAndFlush(httpResponse)
                                        .addListener(ChannelFutureListener.CLOSE);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
