package com.simple.gateway.common.exception;

import com.simple.gateway.common.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户错误请求的异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(ResultCode.BAD_REQUEST);
    }

    public BadRequestException(String messagePattern, Object... arguments) {
        super(ResultCode.BAD_REQUEST, messagePattern, arguments);
    }

}
