package com.simple.gateway.orm.entity.plugin.model;

import com.simple.gateway.orm.entity.plugin.enums.RateLimitScope;
import com.simple.gateway.orm.entity.plugin.enums.RateLimitStrategy;
import lombok.Data;

/**
 * 限流配置
 */
@Data
public class RateLimitConfig {

    /**
     * 限流策略
     */
    RateLimitStrategy strategy = RateLimitStrategy.STANDALONE;

    /**
     * QPS阈值
     */
    Integer qpsValue = 1;

    /**
     * 限流范围
     */
    RateLimitScope scope = RateLimitScope.URI;

}
