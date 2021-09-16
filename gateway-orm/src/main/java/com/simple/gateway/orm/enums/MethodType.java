package com.simple.gateway.orm.enums;

import lombok.Getter;

/**
 * 方法类别
 */
public enum MethodType {

    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete"),
    ;

    @Getter
    private String description;

    MethodType(String description) {
        this.description = description;
    }

}
