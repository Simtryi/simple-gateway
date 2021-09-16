package com.simple.gateway.core.engine.execute;

import com.alibaba.fastjson.TypeReference;
import com.simple.gateway.common.enums.ResultCode;
import com.simple.gateway.common.exception.GatewayException;
import com.simple.gateway.common.util.JsonUtil;
import com.simple.gateway.core.engine.context.ChannelContext;
import com.simple.gateway.core.support.ThreadService;
import com.simple.gateway.core.util.DubboGenericUtil;
import com.simple.gateway.core.util.NettyHttpRequestUtil;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.entity.route.model.RpcParameter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

/**
 * Dubbo 执行器
 */
@Slf4j
@Component
public class DubboExecutor extends AbstractExecutor {

    @Autowired
    ThreadService threadService;

    /**
     * 泛化调用
     */
    @Override
    protected void doExecute(Object... args) {
        if (args == null || args.length <= 0) {
            return;
        }

        Channel channel = (Channel) args[0];

        FullHttpRequest httpRequest = ChannelContext.getHttpRequest(channel);
        NettyHttpRequestUtil nettyHttpRequestUtil = new NettyHttpRequestUtil(httpRequest);
        //获取请求参数
        Map<String, String> params = nettyHttpRequestUtil.getParams();

        Route route = ChannelContext.getRoute(channel);
        //RPC接口
        String interfaceClass = route.getInterfaceClass();
        //RPC方法
        String methodName = route.getMethodName();
        //RPC参数
        String parameters = route.getParameters();

        //参数类型Map
        LinkedHashMap<String, String> parameterTypeMap = new LinkedHashMap<>();
        //参数字段Map
        Map<String, List<String>> parameterFieldMap = new HashMap<>();
        //设置参数类型Map，设置参数字段Map
        fetchParameters(parameters, parameterTypeMap, parameterFieldMap);

        List<String> parameterTypeList = new ArrayList<>();
        List<Object> parameterValueList = new ArrayList<>();

        //处理请求参数
        handleParams(params, parameterTypeList, parameterValueList, parameterTypeMap, parameterFieldMap);

        //RPC参数类型
        String[] parameterTypes = parameterTypeList.toArray(new String[]{});
        //RPC参数值
        Object[] parameterValues = parameterValueList.toArray(new Object[]{});

        Future<Object> future = threadService.getDubboThreadPool().submit(() ->
                DubboGenericUtil.getInstance().genericInvoke(interfaceClass, methodName, parameterTypes, parameterValues));
        try {
            Object result = future.get();
            ChannelContext.setHttpResponseContent(channel, JsonUtil.toJson(result));
        } catch (Throwable throwable) {
            log.error("[DubboExecutor] Dubbo泛化调用错误，{}", throwable.getMessage());
            throw new GatewayException(ResultCode.UNKNOWN, "Dubbo泛化调用错误，{}", throwable.getMessage());
        }
    }

    /**
     * 获取参数，设置参数类型Map，参数字段Map
     *
     * @param parameters            Rpc参数的Json数据
     * @param parameterTypeMap      参数类型Map，key为参数名，value为参数类型，例如："id" => "java.lang.Long"
     * @param parameterFieldMap     参数字段Map，key为参数名，value为参数的字段，例如："student" => ["id","name","age"]
     */
    private void fetchParameters(String parameters, LinkedHashMap<String, String> parameterTypeMap, Map<String, List<String>> parameterFieldMap) {
        TypeReference<ArrayList<RpcParameter>> typeReference = new TypeReference<ArrayList<RpcParameter>>() {};
        ArrayList<RpcParameter> parameterList = JsonUtil.toObject(parameters, typeReference);

        parameterList.forEach(parameter -> {
            parameterTypeMap.put(parameter.getParameterName(), parameter.getParameterType());

            String[] fields = parameter.getParameterField().split(",");
            parameterFieldMap.put(parameter.getParameterName(), new ArrayList<>(Arrays.asList(fields)));
        });
    }

    /**
     * 处理请求参数
     */
    private void handleParams(Map<String, String> params, List<String> parameterTypeList, List<Object> parameterValueList,
                              LinkedHashMap<String, String> parameterTypeMap, Map<String, List<String>> parameterFieldMap) {

        Set<String> paramSet = new HashSet<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            paramSet.add(param.getKey());
        }

        //遍历参数类型Map，参数顺序要和RPC方法一致
        for (Map.Entry<String, String> parameterType : parameterTypeMap.entrySet()) {
            //参数名
            String parameterName = parameterType.getKey();
            //参数类型
            parameterTypeList.add(parameterType.getValue());

            //参数的字段
            List<String> fieldList = parameterFieldMap.get(parameterName);
            if ("".equals(fieldList.get(0))) {
                //普通类型
                if (paramSet.contains(parameterName)) {
                    //从请求params中获取参数值
                    parameterValueList.add(params.get(parameterName));

                    paramSet.remove(parameterName);
                }
            } else {
                //字段Map，key为参数字段，value为字段的值
                Map<String, String> fieldMap = new HashMap<>();

                for (String field : fieldList) {
                    if (paramSet.contains(field)) {
                        //从请求params中获取参数字段值
                        fieldMap.put(field, params.get(field));

                        paramSet.remove(field);
                    }
                }

                parameterValueList.add(fieldMap);
            }
        }

    }

}
