package com.simple.gateway.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.UUID;

/**
 * 字符串处理工具
 */
public class StringUtil extends StringUtils {

    /**
     * 字符串格式化，使用 {} 做占位符
     */
    public static String format(String messagePattern, Object... arguments) {
        return MessageFormatter.arrayFormat(messagePattern, arguments).getMessage();
    }

    /**
     * 生成一个uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * md5加密
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

}
