package com.simple.gateway.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象拷贝工具
 *
 * @author yunda,xing
 * @since 2021/08/26
 */
@UtilityClass
public class BeanCopyUtil {

    /**
     * 将一个list的对象拷贝转化为另外一个类型的list
     *
     * @param ks 原list
     * @param vClass 需要转化为的对象类型
     */
    public <K, V> List<V> transform(List<K> ks, Class<V> vClass) {
        return transform(ks, vClass, (String[]) null);
    }

    /**
     * 将一个list的对象拷贝转化为另外一个类型的list
     *
     * @param ks 原list
     * @param vClass 需要转化为的对象类型
     * @param ignoreFields 忽略的属性
     */
    public <K, V> List<V> transform(List<K> ks, Class<V> vClass, String... ignoreFields) {
        if (CollectionUtils.isEmpty(ks)) {
            return new ArrayList<>();
        }
        List<V> vs = new ArrayList<>();
        try {
            for (K k : ks) {
                if (null == k) {
                    continue;
                }
                V v2 = vClass.getDeclaredConstructor().newInstance();
                if (null == ignoreFields) {
                    BeanUtils.copyProperties(k, v2);
                } else {
                    BeanUtils.copyProperties(k, v2, ignoreFields);
                }
                vs.add(v2);
            }
        } catch (Exception e) {
            throw new RuntimeException("bean copy fail.");
        }
        return vs;
    }

    /** 转化单个类 */
    public <K, V> V transform(K ks, Class<V> vClass) {
        try {
            if (ks == null) {
                return null;
            }
            V vs = vClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(ks, vs);

            return vs;
        } catch (Exception e) {
            throw new RuntimeException("bean copy fail.");
        }
    }

    /** 对象拷贝 忽略某些字段 */
    public <K, V> V transform(K ks, Class<V> vClass, String... ignoreFields) {
        if (null == ks) {
            return null;
        }
        try {
            V vs = vClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(ks, vs, ignoreFields);
            return vs;
        } catch (Exception e) {
            throw new RuntimeException("bean copy fail.");
        }
    }
}
