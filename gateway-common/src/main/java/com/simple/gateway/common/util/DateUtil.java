package com.simple.gateway.common.util;

import com.simple.gateway.common.exception.BaseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具
 */
public class DateUtil {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    /**
     * String 转换为 Date
     */
    public static Date convertStringToDate(String dateString) {

        return convertStringToDate(dateString, DEFAULT_FORMAT);

    }

    public static Date convertStringToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new BaseException("{}无法转换成日期格式", dateString);
        }
    }

    /**
     * Date 转换为 String
     */
    public static String convertDateToString(Date date) {
        return convertDateToString(date, DEFAULT_FORMAT);
    }

    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        return formatter.format(date);
    }

}
