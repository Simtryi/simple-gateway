package com.simple.gateway.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地开发环境工具
 */
@Slf4j
public class LocalEnvUtil {

    private static List<String> localHosts = new ArrayList<String>(){{
        add("eleme-MacBook.local"); //云絮
    }};


    /**
     * @return true表示连接本地数据库，false表示连接服务器数据库
     */
    public static boolean connectLocalDB() {
        String hostname = NetworkUtil.getHostname();
        return NetworkUtil.isLocal() && localHosts.contains(hostname);
    }

    /**
     * 初始化本地环境变量
     */
    public static void initSysProperty() {
        if (!NetworkUtil.isLocal()) {
            return;
        }

        String hostname = NetworkUtil.getHostname();
        log.info("本地环境，开始初始化 system property, hostname:{}", hostname);

        switch (hostname) {
            case "eleme-MacBook.local":
                System.setProperty("jdbcUrl", "jdbc:mysql://127.0.0.1:3306/simple_gateway?useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8");
                System.setProperty("jdbcUsername", "root");
                System.setProperty("jdbcPassword", "12345678");
                break;

            default:
                log.warn("未知本地机器，请自行检查配置");
                break;
        }
    }

}
