package com.simple.gateway.common.exception;

import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常的父类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {

    protected ResultCode code;
    protected String message;


    public BaseException(ResultCode code) {
        super(code.getMessage());

        this.code = code;
        this.message = code.getMessage();
    }

    public BaseException(Throwable throwable) {
        super(throwable);

        this.code = ResultCode.UNKNOWN;
        this.message = throwable.getMessage();
    }

    public BaseException(String messagePattern, Object... arguments) {
        super(StringUtil.format(messagePattern, arguments));

        this.code = ResultCode.UNKNOWN;
        this.message = StringUtil.format(messagePattern, arguments);
    }

    public BaseException(ResultCode code, String messagePattern, Object... arguments) {
        super(StringUtil.format(messagePattern, arguments));

        this.code = code;
        this.message = StringUtil.format(messagePattern, arguments);
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return StringUtil.format("{} code={} message={}", this.getClass().getSimpleName(), this.code, this.message);
    }

}
