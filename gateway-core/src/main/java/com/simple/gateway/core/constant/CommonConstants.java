package com.simple.gateway.core.constant;

import com.simple.gateway.common.util.PropertiesUtil;

/**
 * 网关的公共常量
 */
public interface CommonConstants {

    String PROPERTIES_PATH = "/simple-gateway.properties";

    PropertiesUtil properties = PropertiesUtil.getInstance(PROPERTIES_PATH);

    String HTTP = "HTTP";

    String HTTPS = "HTTPS";

    String FAVICON_ICO = "/favicon.ico";

    String SENTINEL_PREFIX = "GATEWAY_SENTINEL_";

    // ==================== Dubbo 相关常量 开始 ==================== //

    String DUBBO_APPLICATION_NAME = properties.getString("dubbo.application.name");

    String DUBBO_REGISTRY_ADDRESS = properties.getString("dubbo.registry.address");

    String DUBBO_SERVICE_VERSION = properties.getString("dubbo.service.version");

    // ==================== Dubbo 相关常量 结束 ==================== //



    // ==================== Netty 相关常量 开始 ==================== //

    int NETTY_SERVER_PORT = properties.getInt("netty.server.port", 9000);

    // ==================== Netty 相关常量 结束 ==================== //



    // ==================== HttpClient 相关常量 开始 ==================== //

    /**
     * Url 编码格式
     */
    String ENCODING = "UTF-8";

    /**
     * 最大支持的连接数
     */
    int DEFAULT_MAX_TOTAL = 512;

    /**
     * 每个路由的最大连接数
     */
    int DEFAULT_MAX_PER_ROUTE = 64;

    /**
     * 客户端和服务器建立连接的超时时间
     */
    int DEFAULT_CONNECT_TIMEOUT = 6 * 1000;

    /**
     * 等待数据的超时时间
     */
    int DEFAULT_SOCKET_TIMEOUT = 6 * 1000;

    /**
     * 从连接池中获取连接的超时时间
     */
    int DEFAULT_REQUEST_TIMEOUT = 6 * 1000;

    // ==================== HttpClient 相关常量 结束 ==================== //



    // ==================== 线程池 相关常量 开始 ==================== //

    /**
     * 业务线程池核心线程数
     */
    int EVENT_EXECUTOR_POOL_CORE_SIZE =10;

    /**
     * 业务线程池最大线程数
     */
    int EVENT_EXECUTOR_POOL_MAX_SIZE = 20;

    /**
     * 业务线程池临时线程存活时间，单位：s
     */
    int EVENT_EXECUTOR_POOL_KEEP_ALIVE_SECONDS = 10;

    /**
     * 业务线程池阻塞队列
     */
    int EVENT_EXECUTOR_POOL_BLOCKING_QUEUE_SIZE = 10;

    // ==================== 线程池 相关常量 结束 ==================== //



    // ==================== Redis 相关常量 开始 ==================== //

    /**
     * 限流插件前缀
     */
    String RATE_LIMIT_PREFIX = "GATEWAY_RATE_LIMIT_";

    /**
     * 缓存插件前缀
     */
    String CACHE_PREFIX = "GATEWAY_CACHE_";

    /**
     * 最大缓存时间(s)，1h
     */
    int CACHE_MAX_TIME = 60 * 60;

    // ==================== Redis 相关常量 结束 ==================== //

}
