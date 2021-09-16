package com.simple.gateway.core.engine.execute;

import com.simple.gateway.common.exception.GatewayException;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.util.HttpClientUtil;
import com.simple.gateway.core.util.NettyHttpRequestUtil;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.MethodType;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Http 执行器
 */
@Slf4j
@Component
public class HttpExecutor extends AbstractExecutor {

    /**
     * 请求转发
     */
    @Override
    protected void doExecute(Object... args) {
        if (args == null || args.length <= 0) {
            return;
        }

        Channel channel = (Channel) args[0];

        FullHttpRequest httpRequest = ChannelContext.getHttpRequest(channel);
        Route route = ChannelContext.getRoute(channel);

        //获取请求头和请求参数
        NettyHttpRequestUtil nettyHttpRequestUtil = new NettyHttpRequestUtil(httpRequest);
        Map<String, String> headers = nettyHttpRequestUtil.getHeaders();
        Map<String, String> params = nettyHttpRequestUtil.getParams();
        String body = nettyHttpRequestUtil.getPostBody();

        //拼接uri
        String uri = route.getSource() + route.getPath();
        String content = null;

        try {
            if (route.getMethod() == MethodType.GET) {
                content = HttpClientUtil.nettyGet(uri, headers, params);
            } else if (route.getMethod() == MethodType.POST) {
                content = HttpClientUtil.nettyPost(uri, headers, params, body);
            }

            if (content == null) {
                ChannelContext.setHttpResponseContent(channel, "未知错误");
            } else {
                ChannelContext.setHttpResponseContent(channel, content);
            }
        } catch(Exception e) {
            log.error("[HttpExecutor] http请求异常，{}", e.getMessage());
            throw new GatewayException("http请求异常，{}", e.getMessage());
        }
    }

}
