package com.simple.gateway.orm.entity.plugin.model;

import lombok.Data;

/**
 * 黑名单配置
 */
@Data
public class BlackListConfig {

    /**
     * 黑名单列表，以","分隔
     */
    private String blackList = "";

}
