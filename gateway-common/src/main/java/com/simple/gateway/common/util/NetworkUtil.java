package com.simple.gateway.common.util;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络处理工具
 */
public class NetworkUtil {

    private static String hostname;
    private static String ip;


    public synchronized static String getHostname() {
        if (hostname == null) {
            try {
                InetAddress addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        return hostname;
    }

    public synchronized static String getIpAddress() {
        if (ip == null) {
            try {
                InetAddress addr = InetAddress.getLocalHost();
                ip = addr.getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        return ip;
    }

    /**
     * 是否为本地开发环境
     */
    public static boolean isLocal() {
        final String hostname = NetworkUtil.getHostname();
        return StringUtil.endsWith(hostname, "local");
    }

}
