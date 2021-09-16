package com.simple.gateway.core.support;

import com.simple.gateway.common.util.JsonUtil;
import com.simple.gateway.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 根据缓存键获取缓存，没有取到结果返回null
     */
    public String fetchString(String cacheKey) {
        if (StringUtil.isEmpty(cacheKey)) {
            return null;
        }

        // 查看缓存性能
        long startTimestamp = System.currentTimeMillis();

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String value = operations.get(cacheKey);

        long endTimestamp = System.currentTimeMillis();
        long duration = endTimestamp - startTimestamp;

        log.info("[RedisService] 查缓存 {} 耗时：{} 结果: {}", cacheKey, duration, value);

        return value;
    }


    /**
     * 将当前结果集写入到缓存中
     *
     * @param cacheKey    缓存键
     * @param cacheString 缓存字符串
     */
    public void writeString(String cacheKey, String cacheString) {
        long startTimestamp = System.currentTimeMillis();

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(cacheKey, cacheString);

        long endTimestamp = System.currentTimeMillis();
        long duration = endTimestamp - startTimestamp;

        log.info("[RedisService] 缓存 key={} content={} 写入耗时：{}", cacheKey, cacheString, duration);
    }

    /**
     * 将当前结果集写入到缓存中
     *
     * @param cacheKey    缓存键
     * @param cacheString 缓存字符串
     * @param cacheTime   缓存时间(s)
     */
    public void writeString(String cacheKey, String cacheString, int cacheTime) {
        long startTimestamp = System.currentTimeMillis();

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(cacheKey, cacheString, cacheTime, TimeUnit.SECONDS);

        long endTimestamp = System.currentTimeMillis();
        long duration = endTimestamp - startTimestamp;

        log.info("[RedisService] 缓存 key={} content={} 写入耗时：{}", cacheKey, cacheString, duration);
    }

    /**
     * 根据缓存键删除缓存
     */
    public void deleteString(String cacheKey) {
        long startTimestamp = System.currentTimeMillis();

        redisTemplate.delete(cacheKey);

        long endTimestamp = System.currentTimeMillis();
        long duration = endTimestamp - startTimestamp;

        log.info("[RedisService] 缓存 key={} 删除成功，耗时：{}", cacheKey, duration);
    }

    /**
     * 根据缓存键获取缓存，没有取到结果返回null
     */
    public Object fetchObject(String cacheKey) {
        String cacheObjectStr = this.fetchString(cacheKey);
        if (StringUtil.isBlank(cacheObjectStr)) {
            return null;
        } else {
            Object cacheObject = JsonUtil.toObject(cacheObjectStr, Object.class);
            return cacheObject;
        }
    }

    /**
     * 将当前结果集写入到缓存中
     *
     * @param cacheKey     缓存键
     * @param cacheObject  缓存模型
     * @param cacheTime    缓存时间(s)
     */
    public void writeObject(String cacheKey, Object cacheObject, int cacheTime) {
        String cacheObjectStr = JsonUtil.toJson(cacheObject);
        this.writeString(cacheKey, cacheObjectStr, cacheTime);
    }

    /**
     * 对某个key加1，有效时间5s
     *
     * @param key        键值
     * @param upperBound 上限
     * @param size       集群限流的时间(秒)
     * @return true 添加成功  false 触达了上限，操作失败。
     */
    public boolean qpsIncrease(String key, int upperBound, long size) {
        long startTime = System.currentTimeMillis();
        long second = System.currentTimeMillis() / 1000;
        String key4TimeWindow = key + "_" + (second / size);

        boolean underLimit = true;
        boolean success = true;

        try {
            final ValueOperations<String, String> integerValueOperations = redisTemplate.opsForValue();

            String numStr = integerValueOperations.get(key4TimeWindow);
            int num = 0;
            if (StringUtil.isEmpty(numStr)) {
                num = 1;
            } else {
                num = Integer.parseInt(numStr) + 1;
            }

            // 5s过期即可
            integerValueOperations.set(key4TimeWindow, "" + num, 5, TimeUnit.SECONDS);

            if (num > upperBound) {
                log.info("[RedisService] key={} 已经触达了上限 {}", key4TimeWindow, upperBound);
                underLimit = false;
                success = true;
            } else {
                underLimit = true;
                success = true;
            }

        } catch (Throwable throwable) {
            underLimit = true;
            success = false;

            log.error("[RedisService] redis increase error", throwable);

        } finally {
            long cost = System.currentTimeMillis() - startTime;

            log.info("[RedisService] redis自增, key={},success={},underLimit={},cost={},upperBound={}",
                    key4TimeWindow, success, underLimit, cost, upperBound);

        }

        return underLimit;
    }

}