package com.simple.gateway.orm.entity.plugin.model;

import lombok.Data;

/**
 * 白名单配置
 */
@Data
public class WhiteListConfig {

    /**
     * 白名单列表，以","分隔
     */
    private String whiteList = "";

}
