package com.simple.gateway.orm.entity.plugin.model;

import lombok.Data;

/**
 * 缓存配置
 */
@Data
public class CacheConfig {

    /**
     * 缓存时间
     */
    int time = 60;

}
