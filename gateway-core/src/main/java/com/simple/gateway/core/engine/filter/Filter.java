package com.simple.gateway.core.engine.filter;

import io.netty.channel.Channel;

/**
 * 过滤器
 */
public interface Filter {

    /**
     * 过滤
     */
    void filter(Channel channel);

}
