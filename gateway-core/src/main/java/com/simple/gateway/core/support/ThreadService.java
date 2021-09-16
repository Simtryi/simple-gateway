package com.simple.gateway.core.support;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ThreadService {

    private ExecutorService dubboThreadPool = new ThreadPoolExecutor(5, 100, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Dubbo-executor-thread-%d").build());

    private ExecutorService cacheThreadPool = new ThreadPoolExecutor(5, 100, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("Cache-thread-%d").build());



    public ExecutorService getDubboThreadPool() {
        return dubboThreadPool;
    }

    public ExecutorService getCacheThreadPool() {
        return cacheThreadPool;
    }

}
