package com.simple.gateway.common.exception;

import com.simple.gateway.common.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务器异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServerException extends BaseException {

    public ServerException() {
        super(ResultCode.UNKNOWN);
    }

    public ServerException(String messagePattern, Object... arguments) {
        super(ResultCode.UNKNOWN, messagePattern, arguments);
    }

}
