package com.simple.gateway.core.util;

import com.simple.gateway.core.constant.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;


/**
 * Dubbo 泛化调用处理工具
 *
 * <p>
 * 使用单例模式获取 DubboGenericUtil
 * </p>
 */
public class DubboGenericUtil {

    private static DubboGenericUtil instance = new DubboGenericUtil();
    private ApplicationConfig applicationConfig;
    private RegistryConfig registryConfig;
    private String version;

    private DubboGenericUtil() {
        //dubbo consumer 的 application 配置，不设置会直接抛异常
        applicationConfig = new ApplicationConfig();
        applicationConfig.setName(CommonConstants.DUBBO_APPLICATION_NAME);

        registryConfig = new RegistryConfig();
        registryConfig.setAddress(CommonConstants.DUBBO_REGISTRY_ADDRESS);

        version = CommonConstants.DUBBO_SERVICE_VERSION;
    }

    /**
     * 获取 DubboGenericUtil 实例
     */
    public static DubboGenericUtil getInstance() {
        return instance;
    }

    /**
     * Dubbo 泛化调用
     *
     * @param interfaceClass   接口名
     * @param methodName       方法名
     * @param parameterTypes   参数类型
     * @param parameterValues  参数值
     */
    public Object genericInvoke(String interfaceClass, String methodName, String[] parameterTypes, Object[] parameterValues) {
        //ReferenceConfig 实例很重量，里面封装了所有与注册中心及服务提供方连接，需要缓存
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();

        //声明为泛化接口
        referenceConfig.setGeneric(true);
        referenceConfig.setInterface(interfaceClass);
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setVersion(version);

        //使用 Dubbo 提供的缓存
        ReferenceConfigCache configCache = ReferenceConfigCache.getCache();
        GenericService genericService = configCache.get(referenceConfig);

        //调用
        return genericService.$invoke(methodName, parameterTypes, parameterValues);
    }

}
