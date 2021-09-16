package com.simple.gateway.orm.entity.route.model;

import lombok.Data;

/**
 * RPC参数
 */
@Data
public class RpcParameter {

    /**
     * 参数名称
     */
    String parameterName;

    /**
     * 参数类型
     */
    String parameterType;

    /**
     * 参数字段
     */
    String parameterField;

}
