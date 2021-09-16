package com.simple.gateway.orm.entity.plugin.enums;

import lombok.Getter;

/**
 * 限流策略
 */
public enum RateLimitStrategy {

    STANDALONE("单机限流"),
    CLUSTER("集群限流"),
    ;

    @Getter
    private String description;

    RateLimitStrategy(String description) {
        this.description = description;
    }

}
