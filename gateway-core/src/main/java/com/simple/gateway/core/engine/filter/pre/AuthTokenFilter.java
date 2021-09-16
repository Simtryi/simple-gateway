package com.simple.gateway.core.engine.filter.pre;

import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.plugin.model.AuthTokenConfig;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 鉴权过滤器
 */
@Slf4j
@Component
public class AuthTokenFilter extends AbstractFilter {

    @Override
    protected boolean needFilter(Channel channel) {
        Plugin plugin = ChannelContext.getPlugin(channel);
        return plugin.isAllowAuthToken();
    }

    @Override
    public void doFilter(Channel channel) {
        FullHttpRequest httpRequest = ChannelContext.getHttpRequest(channel);
        Plugin plugin = ChannelContext.getPlugin(channel);
        AuthTokenConfig authTokenConfig = plugin.fetchAuthTokenConfig();
        //ToDo 完成鉴权过滤器


        // //用户token
        // String authToken = httpRequest.headers().get("Auth_Token");
        // if (StringUtil.isBlank(authToken)) {
        //     log.info("[AuthTokenFilter] url {} without token, authentication failed", httpRequest.uri());
        //     throw new BadRequestException("url {} without token, authentication failed", httpRequest.uri());
        // }
        // if (!StringUtil.equals(token, authToken)) {
        //     log.info("[AuthTokenFilter] url {} token is wrong, authentication failed", httpRequest.uri());
        //     throw new BadRequestException("url {} token is wrong, authentication failed", httpRequest.uri());
        // } else {
        //     log.info("url {} verify success", httpRequest.uri());
        // }
    }

}
