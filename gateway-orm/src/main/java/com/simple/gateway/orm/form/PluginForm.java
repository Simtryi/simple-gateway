package com.simple.gateway.orm.form;

import com.simple.gateway.orm.base.BaseEntityForm;
import com.simple.gateway.orm.entity.plugin.Plugin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class PluginForm extends BaseEntityForm<Plugin> {

    /**
     * 路由Id
     */
    Long routeId;

    /**
     * 是否使用负载均衡插件
     */
    boolean allowLoadBalance;

    /**
     * 负载均衡配置
     */
    String loadBalanceConfig;

    /**
     * 是否使用鉴权插件
     */
    boolean allowAuthToken;

    /**
     * 鉴权配置
     */
    String authTokenConfig;

    /**
     * 是否使用白名单插件
     */
    boolean allowWhiteList;

    /**
     * 白名单配置
     */
    String whiteListConfig;

    /**
     * 是否使用黑名单插件
     */
    boolean allowBlackList;

    /**
     * 黑名单配置
     */
    String blackListConfig;

    /**
     * 是否使用限流插件
     */
    boolean allowRateLimit;

    /**
     * 限流配置
     */
    String rateLimitConfig;

    /**
     * 是否使用缓存插件
     */
    boolean allowCache;

    /**
     * 是否使用熔断器插件
     */
    boolean allowSentinel;

    /**
     * 熔断器配置
     */
    String sentinelConfig;

    public PluginForm() {
        super(Plugin.class);
    }

    /**
     * 在这里将 form 中的属性赋值给 Plugin，并检查参数合理性。
     */
    protected void preCheck(@NonNull Plugin plugin) {

        plugin.setRouteId(routeId);
        plugin.setAllowLoadBalance(allowLoadBalance);
        plugin.setLoadBalanceConfig(loadBalanceConfig);
        plugin.setAllowAuthToken(allowAuthToken);
        plugin.setAuthTokenConfig(authTokenConfig);
        plugin.setAllowWhiteList(allowWhiteList);
        plugin.setWhiteListConfig(whiteListConfig);
        plugin.setAllowBlackList(allowBlackList);
        plugin.setBlackListConfig(blackListConfig);
        plugin.setAllowRateLimit(allowRateLimit);
        plugin.setRateLimitConfig(rateLimitConfig);
        plugin.setAllowCache(allowCache);
        plugin.setAllowSentinel(allowSentinel);
        plugin.setSentinelConfig(sentinelConfig);

    }

}
