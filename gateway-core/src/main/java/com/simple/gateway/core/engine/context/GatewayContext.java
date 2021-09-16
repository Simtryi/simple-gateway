package com.simple.gateway.core.engine.context;

import com.simple.gateway.common.util.StringUtil;
import lombok.Data;

/**
 * Gateway 请求的上下文
 */
@Data
public class GatewayContext {

    /**
     * 请求的唯一标识
     */
    String code = StringUtil.uuid();

    /**
     * 请求是否命中缓存
     */
    boolean hitCache = false;

}
