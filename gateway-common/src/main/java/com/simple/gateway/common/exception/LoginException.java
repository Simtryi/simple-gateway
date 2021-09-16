package com.simple.gateway.common.exception;

import com.simple.gateway.common.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginException extends BaseException {

    public LoginException() {
        super(ResultCode.LOGIN);
    }

    public LoginException(String messagePattern, Object... arguments) {
        super(ResultCode.LOGIN, messagePattern, arguments);
    }

}
