package com.simple.gateway.orm.entity.route.model;

import com.simple.gateway.orm.enums.MethodType;
import lombok.Data;

import java.util.Map;

/**
 * 路由查询请求
 */
@Data
public class RouteRequest {

    /**
     * 请求方法
     */
    MethodType method;

    /**
     * 请求uri，此处的 uri 为 http://127.0.0.1:9000/xx/yy 格式
     */
    String uri;

    /**
     * 请求头
     */
    Map<String, String> headers;

    /**
     * 请求参数
     */
    Map<String, String> params;

    /**
     * 请求体
     */
    String body;

}
