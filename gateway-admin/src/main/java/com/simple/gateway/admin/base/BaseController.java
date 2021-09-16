package com.simple.gateway.admin.base;

import com.github.pagehelper.Page;
import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.admin.result.Pager;
import com.simple.gateway.admin.result.WebResult;


public abstract class BaseController {

    /**
     * @return 成功消息
     */
    protected WebResult success() {
        return new WebResult(ResultCode.OK, "成功");
    }

    /**
     * @param code    状态码
     * @param message 消息
     * @return 结果消息
     */
    protected WebResult success(ResultCode code, String message) {
        return new WebResult(code, message);
    }

    /**
     * @param object 基础对象
     * @return 对象信息
     */
    protected WebResult success(Object object) {
        WebResult<Object> webResult = new WebResult<>(ResultCode.OK, "成功");
        webResult.setData(object);

        return webResult;
    }

    /**
     * @param pageObj mybatis分页工具
     * @return 对象信息
     */
    protected <T> WebResult success(Page<T> pageObj) {
        Pager<T> pager = new Pager<>(pageObj.getPageNum(), pageObj.getPageSize(), pageObj.getTotal(), pageObj);

        WebResult<Object> webResult = new WebResult<>(ResultCode.OK, "成功");

        webResult.setData(pager);

        return webResult;
    }


}
