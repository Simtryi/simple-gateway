package com.simple.gateway.orm.entity.plugin.model;

import lombok.Data;


/**
 * 鉴权配置
 */
@Data
public class AuthTokenConfig {
    private String code;
    private String userId;
    private String userName;
}
