package com.simple.gateway.orm.entity.route.enums;

import lombok.Getter;

public enum RouteEnv {

    DEV("开发环境"),
    GRAY("灰度环境"),
    PRE("预发环境"),
    PROD("生产环境"),
    ;

    @Getter
    private String description;

    RouteEnv(String description) {
        this.description = description;
    }

}
