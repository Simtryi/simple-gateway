package com.simple.gateway.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.gateway.common.exception.BadRequestException;
import com.simple.gateway.common.exception.ServerException;
import com.simple.gateway.core.service.RouteService;
import com.simple.gateway.core.util.HttpClientUtil;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.entity.route.model.RouteRequest;
import com.simple.gateway.orm.entity.route.model.RouteResponse;
import com.simple.gateway.orm.enums.MethodType;
import com.simple.gateway.orm.form.RouteForm;
import com.simple.gateway.orm.mapper.PluginMapper;
import com.simple.gateway.orm.mapper.RouteMapper;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteMapper routeMapper;

    @Autowired
    PluginMapper pluginMapper;

    @Override
    public Route create(RouteForm routeForm) {
        Route route = routeForm.create();
        routeMapper.insert(route);

        return route;
    }

    @Override
    public Route edit(RouteForm routeForm) {
        Route route = routeForm.update();
        routeMapper.update(route);

        return route;
    }

    @Override
    @Transactional(rollbackFor = ServerException.class)
    public void delete(Long id) {
        Route route = routeMapper.selectById(id);
        if (route != null) {
            routeMapper.deleteById(id);
            log.info("delete route: {}", JSON.toJSONString(route));
        }

        Plugin plugin = pluginMapper.selectByRouteId(id);
        if(plugin != null) {
            pluginMapper.deleteById(plugin.getId());
            log.info("delete plugin: {}", JSON.toJSONString(plugin));
        }
    }

    /**
     * 路由查询
     */
    @Override
    public RouteResponse query(RouteRequest request) {
        MethodType method = request.getMethod();
        String uri = request.getUri();
        Map<String, String> headers = request.getHeaders();
        Map<String, String> params = request.getParams();
        String body = request.getBody();

        RouteResponse routeResponse;
        HttpResponse httpResponse = null;

        try {
            if (method == MethodType.GET) {
                httpResponse = HttpClientUtil.get(uri, headers, params);
            } else if (method == MethodType.POST) {
                httpResponse = HttpClientUtil.post(uri, headers, params, body);
            }

            routeResponse = handleHttpResponse(httpResponse);
        } catch (Exception e) {
            log.error("[RouteService] http请求异常，{}", e.getMessage());
            throw new BadRequestException("http请求异常，{}", e.getMessage());
        }

        return routeResponse;
    }

    /**
     * 处理 HttpResponse
     */
    private RouteResponse handleHttpResponse(HttpResponse httpResponse) throws IOException {
        RouteResponse routeResponse = new RouteResponse();

        if (httpResponse == null) {
            routeResponse.setStatusCode(200);
            routeResponse.setReasonPhrase("OK");
            routeResponse.setData("未知错误");
        } else {
            //设置状态码和原因短语
            StatusLine statusLine = httpResponse.getStatusLine();
            routeResponse.setStatusCode(statusLine.getStatusCode());
            routeResponse.setReasonPhrase(statusLine.getReasonPhrase());

            //设置响应头
            Map<String, String> headers = new HashMap<>();
            Header[] allHeaders = httpResponse.getAllHeaders();
            for (Header header : allHeaders) {
                headers.put(header.getName(), header.getValue());
            }
            routeResponse.setHeaders(headers);

            //设置响应数据
            final HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity, CharsetUtil.UTF_8);
            routeResponse.setData(content);
        }

        return routeResponse;
    }

}
