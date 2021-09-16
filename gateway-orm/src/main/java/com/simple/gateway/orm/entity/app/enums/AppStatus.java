package com.simple.gateway.orm.entity.app.enums;

import lombok.Getter;

/**
 * 应用状态
 */
public enum AppStatus {

    OK("正常"),
    DISABLED("禁用"),
    ;

    @Getter
    private String description;

    AppStatus(String description) {
        this.description = description;
    }

}
