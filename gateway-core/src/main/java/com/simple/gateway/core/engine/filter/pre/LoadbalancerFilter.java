package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.orm.entity.plugin.Plugin;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 负载均衡过滤器
 */
@Slf4j
@Component
public class LoadbalancerFilter extends AbstractFilter {

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return plugin.isAllowLoadBalance();
    }

    @Override
    public void doFilter(Channel channel) {

        //ToDo 完成负载均衡

    }

}
