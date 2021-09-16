package com.simple.gateway.orm.entity.route.enums;

import lombok.Getter;

/**
 * 路由状态
 */
public enum RouteStatus {

    OK("正常"),
    DISABLED("禁用"),
    ;

    @Getter
    private String description;

    RouteStatus(String description) {
        this.description = description;
    }

}
