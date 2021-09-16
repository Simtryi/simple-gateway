package com.simple.gateway.core.engine.filter.post;

import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.core.util.NettyHttpResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 结果过滤器
 */
@Component
@Slf4j
public class ResultFilter extends AbstractFilter {

    @Override
    protected void doFilter(Channel channel) {
        String content = ChannelContext.getHttpResponseContent(channel);
        FullHttpResponse httpResponse = NettyHttpResponseUtil.success(content);
        channel.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }

}
