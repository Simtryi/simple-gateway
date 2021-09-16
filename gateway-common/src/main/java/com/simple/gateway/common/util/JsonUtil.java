package com.simple.gateway.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;



/**
 * Json 处理工具
 */
public class JsonUtil {

    public final static String EMPTY_JSON_OBJECT = "{}";

    /**
     * 将对象转换为 Json 字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return EMPTY_JSON_OBJECT;
        }

        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 将 Json 字符串转换为对象
     */
    public static <T> T toObject(String content, Class<T> clazz) {
        return JSON.parseObject(content, clazz, new Feature[0]);
    }

    /**
     * 将 Json 字符串转换为泛型对象
     */
    public static <T> T toObject(String content, TypeReference<T> typeReference) {
        return JSON.parseObject(content, typeReference);
    }

}
