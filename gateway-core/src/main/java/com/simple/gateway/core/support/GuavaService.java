package com.simple.gateway.core.support;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class GuavaService {

    //秒级缓存
    private LoadingCache<String, Long> qpsCache;

    private LoadingCache<String, Long> getQpsCache() {
        //双重校验
        if (qpsCache == null) {
            synchronized (this) {
                if (qpsCache == null) {
                    CacheLoader<String, Long> qpsCacheLoader = new CacheLoader<String, Long>() {
                        @Override
                        public Long load(String key) throws Exception {
                            return 0L;
                        }
                    };

                    qpsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build(qpsCacheLoader);
                }
            }
        }
        return qpsCache;
    }

    /**
     * 对key加1，key < upperBound 返回 true，key >= upperBound 返回 false
     */
    public boolean qpsIncrease(String key, int upperBound) {
        final LoadingCache<String, Long> qpsCache = getQpsCache();
        long second = System.currentTimeMillis() / 1000;
        String key4TimeWindow = key + "_" + second;

        try {
            final Long value = qpsCache.get(key4TimeWindow);
            if (value >= upperBound) {
                return false;
            } else {
                qpsCache.put(key4TimeWindow, value + 1L);
                return true;
            }
        } catch (Throwable throwable) {
            log.error("在获取单机qps值时出错", throwable);
            return true;
        }
    }

}
