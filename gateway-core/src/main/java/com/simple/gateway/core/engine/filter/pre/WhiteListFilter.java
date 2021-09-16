package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.exception.GatewayException;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.model.WhiteListConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 白名单过滤器
 */
@Slf4j
@Component
public class WhiteListFilter extends AbstractFilter {

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return plugin.isAllowWhiteList();
    }

    @Override
    public void doFilter(Channel channel) {
        FullHttpRequest httpRequest = ChannelContext.getHttpRequest(channel);
        String clientIp = httpRequest.headers().get("X-Forwarded-For");
        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
            clientIp = inetSocketAddress.getAddress().getHostAddress();
        }

        Plugin plugin = ChannelContext.getPlugin(channel);
        WhiteListConfig whiteListConfig = plugin.fetchWhiteListConfig();
        String[] whiteList = whiteListConfig.getWhiteList().split(",");
        for (String ip : whiteList) {
            //客户端ip在白名单中，放行
            if (ip.equals(clientIp)) {
                return;
            }
        }

        log.error("[WhiteListFilter] {} 没有权限访问", clientIp);
        throw new GatewayException(ResultCode.BAD_REQUEST, "{} 没有权限访问", clientIp);
    }

}
