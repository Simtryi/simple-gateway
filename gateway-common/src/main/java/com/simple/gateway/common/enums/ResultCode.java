package com.simple.gateway.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 请求返回的状态码
 */
public enum ResultCode {

    /** 异常状态码 */
    OK(HttpStatus.OK, "成功"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "请求不合法"),
    PARAMS_ERROR(HttpStatus.BAD_REQUEST, "请求参数错误"),
    LOGIN(HttpStatus.UNAUTHORIZED, "未登录，禁止访问"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "没有权限，禁止访问"),
    USER_DISABLED(HttpStatus.UNAUTHORIZED, "账号已注销"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "内容不存在"),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "服务器未知错误"),
    ;

    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String message;

    ResultCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
