package com.simple.gateway.core.engine.filter.error;

import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.core.util.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.springframework.stereotype.Component;

/**
 * 错误过滤器
 */
@Component
public class ErrorFilter extends AbstractFilter {

    @Override
    public void doFilter(Channel channel) {
        Throwable exception = ChannelContext.getException(channel);
        if (exception != null) {

            //ToDo 增加告警通知
            channel.writeAndFlush(NettyHttpResponseUtil.fail(exception.getMessage()))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

}
