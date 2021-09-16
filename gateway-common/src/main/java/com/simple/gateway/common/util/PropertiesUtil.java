package com.simple.gateway.common.util;

import com.simple.gateway.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件处理工具
 *
 * <p>
 * 使用单例模式，获取配置文件中的信息
 * </p>
 */
@Slf4j
public class PropertiesUtil {
    
    private static Map<String, PropertiesUtil> propertiesUtilsHolder;

    private static Map<PropertiesUtil, Properties> propertiesMap;

    private volatile boolean propertiesLoaded;

    static{
        propertiesUtilsHolder = new HashMap<>();
        propertiesMap = new HashMap<>();
    }



    private PropertiesUtil() {
        
    }

    /**
     * 获取 PropertiesUtil 实例，一个 propertiesPath 一个 PropertiesUtil 实例
     */
    public static synchronized PropertiesUtil getInstance(String propertiesPath) {
        PropertiesUtil propertiesUtil = propertiesUtilsHolder.get(propertiesPath);

        if(propertiesUtil == null){
            log.info("[PropertiesUtil] instance is null with propertiesPath={}, will create a new instance directly.", propertiesPath);

            InputStream inputStream = null;

            try{
                propertiesUtil = new PropertiesUtil();
                Properties properties = new Properties();
                inputStream = PropertiesUtil.class.getResourceAsStream(propertiesPath);

                if(inputStream != null){
                    properties.load(inputStream);
                    propertiesUtilsHolder.put(propertiesPath, propertiesUtil);
                    propertiesMap.put(propertiesUtil, properties);

                    log.info("[PropertiesUtil] instance init success.");
                    propertiesUtil.propertiesLoaded = true;
                }
            } catch (Exception e) {
                log.error("[PropertiesUtil] getInstance error,cause:{}",e.getMessage(),e);
                throw new ServerException("[PropertiesUtil] getInstance error, cause:{}", e.getMessage());
            } finally{
                try {
                    if(inputStream != null){
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return propertiesUtil;
    }

    /**
     * 根据 Resource 获取 properties
     */
    public static Properties getPropertiesByResource(String propertiesPath){
        InputStream inputStream = null;
        Properties properties = null;

        try{
            inputStream = PropertiesUtil.class.getResourceAsStream(propertiesPath);
            if(inputStream != null){
                properties = new Properties();
                properties.load(inputStream);
            }
        } catch (Exception e) {
            log.error("getInstance occur error,cause:",e);
            throw new ServerException("getInstance occur error, cause: {}", e.getMessage());
        } finally{
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return properties;
    }

    /**
     * 判断是否加载完毕
     */
    private boolean isPropertiesLoaded(){
        int retryTime = 0;
        int retryTimeout = 1000;
        int sleep = 500;

        while(!propertiesLoaded && retryTime < retryTimeout){
            try {
                Thread.sleep(sleep);
                retryTime+=sleep;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return propertiesLoaded;
    }

    /**
     * 获得配置信息的 String 值
     */
    public String getString(String key){
        if(isPropertiesLoaded()){
            Properties properties = propertiesMap.get(this);
            return null != properties ? properties.getProperty(key) : null;
        }

        return null;
    }

    /**
     * 获得配置信息的 String 数组
     */
    public String[] getStringArray(String key) {
        String value = getString(key);

        if (StringUtils.isBlank(value)) {
            return null;
        }

        return StringUtils.split(value, ",");
    }

    /**
     * 获得配置信息的 boolean 值
     */
    public boolean getBoolean(String key){
        String value = getString(key);

        return "true".equalsIgnoreCase(value);
    }

    /**
     * 获得配置信息的 int 值
     */
    public int getInt(String key, int defaultValue){
        String value = getString(key);
        int intValue;

        try{
            intValue = Integer.parseInt(value);
        }catch(Exception e){
            intValue = defaultValue;
        }

        return intValue;
    }

    /**
     * 获得配置信息的 long 值
     */
    public long getLong(String key, long defaultValue){
        String value = getString(key);
        long longValue;

        try{
            longValue = Long.parseLong(value);
        }catch(Exception e){
            longValue = defaultValue;
        }

        return longValue;
    }

}
