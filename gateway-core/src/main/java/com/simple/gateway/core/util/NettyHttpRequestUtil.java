package com.simple.gateway.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simple.gateway.common.util.PrimitiveTypeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Netty 的 HttpRequest 处理工具
 */
public class NettyHttpRequestUtil {

    private HttpRequest httpRequest;

    public NettyHttpRequestUtil(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }


    /**
     * 获取请求头
     */
    public Map<String, List<String>> getHeader() {
        Map<String, List<String>> header = new HashMap<>();
        httpRequest.headers().entries().forEach(entry-> {
            List<String> values = header.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
            values.add(entry.getValue());
        });
        return header;
    }

    /**
     * 获取请求的 headers
     */
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>(16);

        httpRequest.headers().forEach(entry -> headers.put(entry.getKey(), entry.getValue()));

        return headers;
    }

    /**
     * 获取请求参数
     */
    public Map<String, String> getParams() {
        Map<String, String> res = new HashMap<>();
        Map<String, List<String>> params = new HashMap<>();

        HttpMethod method = httpRequest.method();
        FullHttpRequest fullHttpRequest = (FullHttpRequest) httpRequest;

        if (Objects.equals(HttpMethod.GET, method)) {
            params = getGetParams(fullHttpRequest);
        } else if (Objects.equals(HttpMethod.POST, method)) {
            params = getPostParams(fullHttpRequest);
        }

        for (Map.Entry<String, List<String>> param : params.entrySet()) {
            res.put(param.getKey(), param.getValue().get(0));
        }

        return res;
    }

    /**
     * 获取 GET 请求参数
     */
    private Map<String, List<String>> getGetParams(FullHttpRequest httpRequest) {
        QueryStringDecoder stringDecoder = new QueryStringDecoder(httpRequest.uri(), CharsetUtil.UTF_8);
        return stringDecoder.parameters();
    }

    /**
     * 获取 POST 请求参数
     */
    private Map<String, List<String>> getPostParams(FullHttpRequest httpRequest) {
        Map<String, List<String>> params = new HashMap<>();
        String contentType = getContentType(httpRequest.headers());
        String content = httpRequest.content().toString(CharsetUtil.UTF_8);

        if (HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)) {
            JSONObject obj = JSON.parseObject(content);

            for (Map.Entry<String, Object> item : obj.entrySet()) {
                convert2Map(params, item.getKey(), item.getValue());
            }
        } else if (HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString().equals(contentType)) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(content);
            params = queryStringDecoder.parameters();
        }

        return params;
    }

    /**
     * 获取 POST 请求体
     */
    public String getPostBody() {
        FullHttpRequest fullHttpRequest = (FullHttpRequest) httpRequest;
        return fullHttpRequest.content().toString(CharsetUtil.UTF_8);
    }

    /**
     * 获取 ContentType
     */
    public String getContentType(HttpHeaders headers) {
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);

        if (StringUtils.containsIgnoreCase(contentType, ";")) {
            String[] strings = contentType.split(";");
            return strings[0];
        } else {
            return contentType;
        }
    }

    /**
     * 格式转换
     */
    private void convert2Map(Map<String, List<String>> params, String key, Object value) {
        List<String> valueList;
        if (params.containsKey(key)) {
            valueList = params.get(key);
        } else {
            valueList = new ArrayList<>();
        }

        Class<?> valueClass = value.getClass();
        if (PrimitiveTypeUtil.isPriType(valueClass)) {
            //是否为基本类型
            valueList.add(value.toString());

            params.put(key, valueList);
        } else if (valueClass.isArray()) {
            //是否为数组类型
            int length = Array.getLength(value);
            for(int i=0; i<length; i++){
                String arrayItem = String.valueOf(Array.get(value, i));
                valueList.add(arrayItem);
            }

            params.put(key, valueList);
        } else if (List.class.isAssignableFrom(valueClass)) {
            //是否为List类型
            if(valueClass.equals(JSONArray.class)){
                JSONArray jArray = JSONArray.parseArray(value.toString());
                for(int i=0; i<jArray.size(); i++){
                    valueList.add(jArray.getString(i));
                }
            }else{
                valueList = (ArrayList<String>) value;
            }

            params.put(key, valueList);
        }else if(Map.class.isAssignableFrom(valueClass)){
            //是否为Map类型
            Map<String, String> tempMap = (Map<String, String>) value;
            tempMap.forEach((k,v)->{
                List<String> tempList = new ArrayList<>();
                tempList.add(v);
                params.put(k, tempList);
            });
        }
    }

}
