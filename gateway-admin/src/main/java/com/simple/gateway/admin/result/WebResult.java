package com.simple.gateway.admin.result;

import com.simple.gateway.common.enums.ResultCode;
import lombok.Data;

/**
 * 出参统一格式
 */
@Data
public class WebResult<T> {

    private ResultCode code = ResultCode.OK;
    private String msg = "success";
    private T data;

    public WebResult(T data) {
        this.data = data;
    }

    public WebResult(ResultCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WebResult(ResultCode code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
