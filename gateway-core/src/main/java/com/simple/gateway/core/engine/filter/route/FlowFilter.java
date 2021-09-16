package com.simple.gateway.core.engine.filter.route;

import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.engine.context.GatewayContext;
import com.simple.gateway.core.engine.execute.DubboExecutor;
import com.simple.gateway.core.engine.execute.HttpExecutor;
import com.simple.gateway.core.engine.filter.AbstractFilter;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.ProtocolType;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分流过滤器
 *
 * <p>
 * 按照协议进行分流
 * </p>
 */
@Slf4j
@Component
public class FlowFilter extends AbstractFilter {

    @Autowired
    DubboExecutor dubboExecutor;

    @Autowired
    HttpExecutor httpExecutor;

    @Override
    protected boolean needFilter(Channel channel) {
        GatewayContext gatewayContext = ChannelContext.getGatewayContext(channel);

        //命中缓存不需要分流
        return !gatewayContext.isHitCache();
    }

    @Override
    public void doFilter(Channel channel) {
        Route route = ChannelContext.getRoute(channel);

        if (route.getProtocol() == ProtocolType.DUBBO) {
            dubboExecutor.execute(channel);
        } else if (route.getProtocol() == ProtocolType.HTTP){
            httpExecutor.execute(channel);
        }
    }

}
