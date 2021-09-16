package com.simple.gateway.orm.entity.plugin.model;

import com.simple.gateway.orm.entity.plugin.enums.SentinelRule;
import lombok.Data;

/**
 * 熔断器配置
 */
@Data
public class SentinelConfig {

    /**
     * 熔断降级规则
     */
    SentinelRule rule = SentinelRule.EXCEPTION_COUNT;

    /**
     * 平均响应时间阈值(ms)
     */
    Double rtValue = 1000D;

    /**
     * 异常比例阈值 [0.0, 1.0]
     */
    Double exceptionRatioValue = 0.3D;

    /**
     * 异常数阈值
     */
    Double exceptionCountValue = 3D;

    /**
     * 时间窗口(s)，其中 EXCEPTION_COUNT 规则的时间窗口可以设置的最小值是 1m
     */
    int timeWindow = 60;

    /**
     * 提示内容
     */
    String message = "service degrade";

}
