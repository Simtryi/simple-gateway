package com.simple.gateway.orm.entity.plugin.enums;

import lombok.Getter;

public enum RateLimitScope {

    URI("URI限流"),
    IP("IP限流"),
    URI_IP("URI和IP限流"),
    ;

    @Getter
    private String description;

    RateLimitScope(String description) {
        this.description = description;
    }

}
