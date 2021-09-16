package com.simple.gateway.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则处理工具
 */
public class RegexUtil {

    /**
     * 验证一个字符串是否符合某个正则表达式
     * 如果提供的正则表达式中没有 ^ $，会默认加上
     */
    public static boolean match(String string, String regex) {
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        return matcher.matches();
    }

}
