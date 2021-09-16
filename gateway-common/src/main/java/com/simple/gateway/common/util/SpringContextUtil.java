package com.simple.gateway.common.util;

import com.simple.gateway.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

/**
 * 获取 Spring Bean
 */
@Slf4j
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    public static ApplicationContext getAppContext() {
        return appCtx;
    }


    public static Object getBean(String beanName) throws BeansException {
        return getAppContext().getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return getAppContext().getBean(requiredType);
    }

    /**
     * 获取带泛型的bean
     */
    public static Object getGenericBean(Class<?> sourceClass, Class<?>... generics) {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(sourceClass, generics);

        if (resolvableType != null) {
            String[] beanNames = SpringContextUtil.getAppContext().getBeanNamesForType(resolvableType);
            if (beanNames != null) {
                if (beanNames.length > 0 && beanNames[0] != null) {
                    return SpringContextUtil.getAppContext().getBean(beanNames[0]);
                } else {
                    log.error("出现数组长度为0的情况了！");
                }

            }

        }

        throw new ServerException("不存在" + resolvableType.toString() + "的相关Bean,请及时排查错误！");
    }

}
