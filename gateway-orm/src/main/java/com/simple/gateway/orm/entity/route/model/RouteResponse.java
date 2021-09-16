package com.simple.gateway.orm.entity.route.model;

import lombok.Data;

import java.util.Map;

/**
 * 路由查询响应
 */
@Data
public class RouteResponse {

    /**
     * 状态码
     */
    int statusCode;

    /**
     * 原因短语
     */
    String reasonPhrase;

    /**
     * 响应头
     */
    Map<String, String> headers;

    /**
     * 响应数据
     */
    String data;

}
