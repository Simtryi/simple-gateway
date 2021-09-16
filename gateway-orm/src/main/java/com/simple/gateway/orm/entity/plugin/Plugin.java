package com.simple.gateway.orm.entity.plugin;

import com.simple.gateway.common.util.JsonUtil;
import com.simple.gateway.orm.base.BaseEntity;
import com.simple.gateway.orm.entity.plugin.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Plugin extends BaseEntity {

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
     * 缓存配置
     */
    String cacheConfig;

    /**
     * 是否使用熔断器插件
     */
    boolean allowSentinel;

    /**
     * 熔断器配置
     */
    String sentinelConfig;

    /**
     * 获取负载均衡配置
     * 如果为空，那么返回一个默认的。
     */
    public LoadBalanceConfig fetchLoadBalanceConfig() {
        LoadBalanceConfig loadBalanceConfig = null;

        try {
            loadBalanceConfig = JsonUtil.toObject(this.getLoadBalanceConfig(), LoadBalanceConfig.class);
        } catch (Throwable ignored) {
        }

        if (loadBalanceConfig == null) {
            loadBalanceConfig = new LoadBalanceConfig();
        }

        return loadBalanceConfig;
    }

    /**
     * 获取鉴权配置
     * 如果为空，那么返回一个默认的。
     */
    public AuthTokenConfig fetchAuthTokenConfig() {
        AuthTokenConfig authTokenConfig = null;

        try {
            authTokenConfig = JsonUtil.toObject(this.getAuthTokenConfig(), AuthTokenConfig.class);
        } catch (Throwable ignored) {
        }

        if (authTokenConfig == null) {
            authTokenConfig = new AuthTokenConfig();
        }

        return authTokenConfig;
    }

    /**
     * 获取白名单配置
     * 如果为空，那么返回一个默认的。
     */
    public WhiteListConfig fetchWhiteListConfig() {
        WhiteListConfig whiteListConfig = null;

        try {
            whiteListConfig = JsonUtil.toObject(this.getWhiteListConfig(), WhiteListConfig.class);
        } catch (Throwable ignored) {
        }

        if (whiteListConfig == null) {
            whiteListConfig = new WhiteListConfig();
        }

        return whiteListConfig;
    }

    /**
     * 获取黑名单配置
     * 如果为空，那么返回一个默认的。
     */
    public BlackListConfig fetchBlackListConfig() {
        BlackListConfig blackListConfig = null;

        try {
            blackListConfig = JsonUtil.toObject(this.getBlackListConfig(), BlackListConfig.class);
        } catch (Throwable ignored) {
        }

        if (blackListConfig == null) {
            blackListConfig = new BlackListConfig();
        }

        return blackListConfig;
    }

    /**
     * 获取限流配置
     * 如果为空，那么返回一个默认的。
     */
    public RateLimitConfig fetchRateLimitConfig() {
        RateLimitConfig rateLimitConfig = null;

        try {
            rateLimitConfig = JsonUtil.toObject(this.getRateLimitConfig(), RateLimitConfig.class);
        } catch (Throwable ignored) {
        }

        if (rateLimitConfig == null) {
            rateLimitConfig = new RateLimitConfig();
        }

        return rateLimitConfig;
    }

    /**
     * 获取缓存配置
     * 如果为空，那么返回一个默认的。
     */
    public CacheConfig fetchCacheConfig() {
        CacheConfig cacheConfig = null;

        try {
            cacheConfig = JsonUtil.toObject(this.getCacheConfig(), CacheConfig.class);
        } catch (Throwable ignored) {
        }

        if (cacheConfig == null) {
            cacheConfig = new CacheConfig();
        }

        return cacheConfig;
    }

    /**
     * 获取熔断器配置
     * 如果为空，那么返回一个默认的。
     */
    public SentinelConfig fetchSentinelConfig() {
        SentinelConfig sentinelConfig = null;

        try {
            sentinelConfig = JsonUtil.toObject(this.getSentinelConfig(), SentinelConfig.class);
        } catch (Throwable ignored) {
        }

        if (sentinelConfig == null) {
            sentinelConfig = new SentinelConfig();
        }

        return sentinelConfig;
    }

}
