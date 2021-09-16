package com.simple.gateway.core.util;

import cn.hutool.core.util.CharsetUtil;
import com.simple.gateway.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * HttpClient 处理工具
 */
@Slf4j
public class HttpClientUtil {

    private static PoolingHttpClientConnectionManager httpClientPool = new PoolingHttpClientConnectionManager();

    static {
        httpClientPool.setMaxTotal(CommonConstants.DEFAULT_MAX_TOTAL);
        httpClientPool.setDefaultMaxPerRoute(CommonConstants.DEFAULT_MAX_PER_ROUTE);
        httpClientPool.setValidateAfterInactivity(6000);
    }



    public static HttpResponse get(String uri) throws Exception {
        return get(uri, null, null);
    }

    public static HttpResponse get(String uri, Map<String, String> params) throws Exception {
        return get(uri, null, params);
    }

    public static HttpResponse get(String uri, Map<String, String> headers, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(uri);

        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                uriBuilder.setParameter(param.getKey(), param.getValue());
            }
        }

        //创建 Http 对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        //设置请求的配置
        httpGet.setConfig(getRequestConfig());

        //设置请求头
        setHeaders(headers, httpGet);

        //创建 HttpClient 对象
        CloseableHttpClient httpClient = getHttpClient();

        //获取响应结果
        return getHttpResponse(httpClient, httpGet);
    }

    public static String nettyGet(String uri, Map<String, String> headers, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(uri);

        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                uriBuilder.setParameter(param.getKey(), param.getValue());
            }
        }

        //创建 Http 对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        //设置请求的配置
        httpGet.setConfig(getRequestConfig());

        //设置请求头
        setHeaders(headers, httpGet);

        //创建 HttpClient 对象
        CloseableHttpClient httpClient = getHttpClient();

        //获取响应结果
        return getNettyHttpResponse(httpClient, httpGet);
    }

    public static HttpResponse post(String uri) throws Exception {
        return post(uri, null);
    }

    public static HttpResponse post(String uri, Map<String, String> params) throws Exception {
        return post(uri, null, params);
    }

    public static HttpResponse post(String uri, Map<String, String> headers, Map<String, String> params) throws Exception {
        return post(uri, headers, params, null);
    }

    public static HttpResponse post(String uri, Map<String, String> headers, Map<String, String> params, String body) throws Exception {
        //创建 Http 对象
        HttpPost httpPost = new HttpPost(uri);

        //设置请求的配置
        httpPost.setConfig(getRequestConfig());

        //设置请求头
        setHeaders(headers, httpPost);

        //设置请求参数
        setParams(params, httpPost);

        //设置请求体
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(body, CharsetUtil.UTF_8));

        //创建 HttpClient 对象
        CloseableHttpClient httpClient = getHttpClient();

        //获取响应结果
        return getHttpResponse(httpClient, httpPost);
    }

    public static String nettyPost(String uri, Map<String, String> headers, Map<String, String> params, String body) throws Exception {
        //创建 Http 对象
        HttpPost httpPost = new HttpPost(uri);

        //设置请求的配置
        httpPost.setConfig(getRequestConfig());

        //设置请求头
        setHeaders(headers, httpPost);

        //设置请求参数
        setParams(params, httpPost);

        //设置请求体
        Header[] contentLengthHeader = httpPost.getHeaders("Content-Length");
        if (contentLengthHeader != null) {
            httpPost.removeHeaders("Content-Length");
        }
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(body, CharsetUtil.UTF_8));

        //创建 HttpClient 对象
        CloseableHttpClient httpClient = getHttpClient();

        //获取响应结果
        return getNettyHttpResponse(httpClient, httpPost);
    }

    /**
     * 获取响应结果
     */
    private static HttpResponse getHttpResponse(CloseableHttpClient httpClient, HttpRequestBase httpMethod) {
        long startTime = System.currentTimeMillis();
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
            return httpResponse;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            log.info("[HttpClientUtil] http请求耗时，{}s", cost / 1000);
        }
    }

    private static String getNettyHttpResponse(CloseableHttpClient httpClient, HttpRequestBase httpMethod) {
        long startTime = System.currentTimeMillis();
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethod)) {
            //获取响应数据
            final HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, CharsetUtil.UTF_8);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            log.info("[HttpClientUtil] http请求耗时，{}s", cost / 1000);
        }
    }

    /**
     * 获取 HttpClient
     */
    private static CloseableHttpClient getHttpClient() {
        //创建HttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpClientPool)
                .build();

        return httpClient;
    }

    /**
     * 获取请求的配置
     */
    private static RequestConfig getRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CommonConstants.DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(CommonConstants.DEFAULT_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(CommonConstants.DEFAULT_REQUEST_TIMEOUT)
                .build();

        return requestConfig;
    }

    /**
     * 设置请求头
     */
    private static void setHeaders(Map<String, String> headers, HttpRequestBase httpRequestBase) {
        if (headers == null) {
            return;
        }

        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpRequestBase.setHeader(header.getKey(), header.getValue());
        }
    }

    /**
     * 设置请求体
     */
    private static void setParams(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if (params == null) {
            return;
        }

        List<NameValuePair> nameValuePairs = new ArrayList<>();

        for (Map.Entry<String, String> param : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }

        httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, CommonConstants.ENCODING));
    }

}
