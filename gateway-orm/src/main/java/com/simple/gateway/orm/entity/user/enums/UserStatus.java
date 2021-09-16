package com.simple.gateway.orm.entity.user.enums;

import lombok.Getter;

/**
 * 用户状态
 */
public enum UserStatus {

    OK("正常"),
    FORBIDDEN("禁用"),
    ;

    @Getter
    private String description;

    UserStatus(String description) {
        this.description = description;
    }

}
