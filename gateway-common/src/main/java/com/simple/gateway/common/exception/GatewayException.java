package com.simple.gateway.common.exception;

import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网关异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayException extends RuntimeException {

    protected ResultCode code;
    protected String message;


    public GatewayException(ResultCode code) {
        super(code.getMessage());

        this.code = code;
        this.message = code.getMessage();
    }

    public GatewayException(Throwable throwable) {
        super(throwable);

        this.code = ResultCode.UNKNOWN;
        this.message = throwable.getMessage();
    }

    public GatewayException(String messagePattern, Object... arguments) {
        super(StringUtil.format(messagePattern, arguments));

        this.code = ResultCode.UNKNOWN;
        this.message = StringUtil.format(messagePattern, arguments);
    }

    public GatewayException(ResultCode resultCode, String messagePattern, Object... arguments) {
        super(StringUtil.format(messagePattern, arguments));

        this.code = resultCode;
        this.message = StringUtil.format(messagePattern, arguments);
    }

    @Override
    public String toString() {
        return StringUtil.format("{} code={} message={}", this.getClass().getSimpleName(), this.code, this.message);
    }
    
}
