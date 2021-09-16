package com.simple.gateway.admin.exception;

import com.simple.gateway.admin.result.WebResult;
import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.exception.BaseException;
import com.simple.gateway.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //请求参数不符合要求
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public WebResult handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        String message = StringUtil.format("要求填写的参数{}({})不符合要求", exception.getParameterName(), exception.getParameterType());
        WebResult webResult = new WebResult(ResultCode.PARAMS_ERROR, message);

        log.error("[GlobalExceptionHandler] {}", ExceptionUtils.getRootCauseMessage(exception));

        return webResult;
    }

    //自定义的异常
    @ExceptionHandler({BaseException.class})
    @ResponseBody
    public WebResult handleUtilException(BaseException exception) {
        //按照各自的异常进行返回。
        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());

        log.error("[GlobalExceptionHandler] {}", ExceptionUtils.getRootCauseMessage(exception));

        return webResult;
    }

    //其他不能处理的异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public WebResult handleException(Throwable throwable) {
        WebResult webResult = new WebResult(ResultCode.UNKNOWN, StringUtil.format("未知错误，{}", ExceptionUtils.getRootCauseMessage(throwable)));

        log.error("[GlobalExceptionHandler] {}", ExceptionUtils.getRootCauseMessage(throwable));

        return webResult;
    }

}
