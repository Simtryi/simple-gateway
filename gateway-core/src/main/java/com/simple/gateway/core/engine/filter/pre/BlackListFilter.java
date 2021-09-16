package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.exception.GatewayException;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.model.BlackListConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 黑名单过滤器
 */
@Slf4j
@Component
public class BlackListFilter extends AbstractFilter {

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return plugin.isAllowBlackList();
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
        BlackListConfig blackListConfig = plugin.fetchBlackListConfig();
        String[] blackList = blackListConfig.getBlackList().split(",");
        for (String ip : blackList) {
            //客户端ip在黑名单中，报错
            if (ip.equals(clientIp)) {
                log.error("[BlackListFilter] {} 禁止访问", clientIp);
                throw new GatewayException(ResultCode.BAD_REQUEST, "{} 禁止访问", clientIp);
            }
        }
    }

}
