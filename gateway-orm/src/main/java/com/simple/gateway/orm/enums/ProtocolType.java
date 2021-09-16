package com.simple.gateway.orm.enums;


import lombok.Getter;


/**
 * 协议类别
 */
public enum ProtocolType {

    HTTP("http"),
    DUBBO("dubbo"),
    ;

    @Getter
    private String description;

    ProtocolType(String description) {
        this.description = description;
    }

}
