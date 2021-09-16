package com.simple.gateway.orm.enums;

import lombok.Getter;

public enum SortDirection {

    ASC("升序"),
    DESC("降序"),
    ;

    @Getter
    private String description;

    SortDirection(String description) {
        this.description = description;
    }

}
