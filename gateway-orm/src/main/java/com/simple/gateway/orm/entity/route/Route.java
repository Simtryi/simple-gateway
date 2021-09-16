package com.simple.gateway.orm.entity.route;


import com.simple.gateway.orm.base.BaseEntity;
import com.simple.gateway.orm.enums.MethodType;
import com.simple.gateway.orm.enums.ProtocolType;
import com.simple.gateway.orm.entity.route.enums.RouteEnv;
import com.simple.gateway.orm.entity.route.enums.RouteStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 路由
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Route extends BaseEntity {

    /**
     * 应用Id
     */
    Long appId;

    /**
     * 路由协议
     */
    ProtocolType protocol;

    /**
     * 路由方法
     */
    MethodType method;

    /**
     * 路由uri /application/path
     */
    String uri;

    /**
     * 路由路径
     */
    String path;

    /**
     * 路由版本
     */
    String version;

    /**
     * Http源站
     */
    String source;

    /**
     * RPC应用
     */
    String application;

    /**
     * RPC接口
     */
    String interfaceClass;

    /**
     * RPC方法名
     */
    String methodName;

    /**
     * RPC参数
     * {@link com.simple.gateway.orm.entity.route.model.RpcParameter}[]
     * 例如：[{parameterName: "student", parameterType: "com.simple.gateway.dubbodemo.entity.Student", parameterField: "id,name,age"}, {...}]
     */
    String parameters;

    /**
     * 超时时间
     */
    Long timeout;

    /**
     * 路由状态 OK: 正常，DISABLED: 禁用
     */
    RouteStatus status;

    /**
     * 路由环境 DEV: 开发，PRE: 预发，GRAY: 灰度，PROD: 生产
     */
    RouteEnv env;

    /**
     * 路由标签, 以 "," 号分隔
     */
    String tags;

    /**
     * 路由描述
     */
    String description;

}
