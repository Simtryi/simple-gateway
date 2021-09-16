package com.simple.gateway.core.util;

import com.simple.gateway.common.exception.ServerException;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp 处理工具
 */
public class OkHttpUtil {

    private OkHttpClient okHttpClient;
    private static final MediaType JSON = MediaType.parse("application/json");
    private static final OkHttpUtil instance = new OkHttpUtil();

    public OkHttpUtil() {
        this.okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpUtil getInstance() {
        return instance;
    }

    /**
     * get 请求
     */
    public String get(String url, Map<String, Object> headers) {
        Request request = getRequest(url, headers);

        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ServerException("[OkHttpUtil] okhttp response failed");
            }

            String responseBody = response.body().string();
            return responseBody;
        } catch (IOException e) {
            throw new ServerException("[OkHttpUtil] okhttp io exception, {}", e.getMessage());
        }
    }

    /**
     * post 请求
     */
    public String post(String url, Map<String, Object> headers, String content) {
        Request request = getRequest(url, headers, content);

        try (Response response = this.okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ServerException("[OkHttpUtil] okhttp response failed");
            }

            return response.body().string();
        } catch (IOException e) {
            throw new ServerException("[OkHttpUtil] okhttp io exception, {}", e.getMessage());
        }
    }

    /**
     * 获取 Request
     */
    private Request getRequest(String url, Map<String, Object> headers) {
        Request request = new Request.Builder().url(url).build();

        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> request.newBuilder().addHeader(k, String.valueOf(v)));
        }

        return request;
    }

    private Request getRequest(String url, Map<String, Object> headers, String content) {
        RequestBody body = RequestBody.create(JSON, content);
        Request request = getRequest(url, headers);

        return request.newBuilder().post(body).build();
    }

}
