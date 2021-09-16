package com.simple.gateway.orm.entity.plugin.enums;

import lombok.Getter;

/**
 * 降级规则
 */
public enum SentinelRule {

    RT("平均响应时间"),
    EXCEPTION_RATIO("异常比例"),
    EXCEPTION_COUNT("异常数量"),
    ;

    @Getter
    private String description;

    SentinelRule(String description) {
        this.description = description;
    }

}
