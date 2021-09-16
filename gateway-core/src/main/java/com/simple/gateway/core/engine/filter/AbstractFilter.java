package com.simple.gateway.core.engine.filter;


import io.netty.channel.Channel;

/**
 * Filter 的抽象类
 */
public abstract class AbstractFilter implements Filter {

    @Override
    public void filter(Channel channel) {
        if (this.needFilter(channel)) {
            this.doFilter(channel);
        }
    }

    /**
     * 执行具体的过滤
     */
    protected abstract void doFilter(Channel channel);

    /**
     * 是否需要过滤，默认都需要过滤
     */
    protected boolean needFilter(Channel channel) {
        return true;
    }

}
