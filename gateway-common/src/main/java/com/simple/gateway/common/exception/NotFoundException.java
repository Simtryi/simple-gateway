package com.simple.gateway.common.exception;

import com.simple.gateway.common.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户请求的资源不存在
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends BaseException {

    public NotFoundException() {
        super(ResultCode.NOT_FOUND);
    }

    public NotFoundException(String messagePattern, Object... arguments) {
        super(ResultCode.NOT_FOUND, messagePattern, arguments);
    }

}
