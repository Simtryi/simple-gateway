package com.simple.gateway.orm.form;

import com.simple.gateway.common.exception.BadRequestException;
import com.simple.gateway.common.util.SpringContextUtil;
import com.simple.gateway.common.util.StringUtil;
import com.simple.gateway.orm.base.BaseEntityForm;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.MethodType;
import com.simple.gateway.orm.enums.ProtocolType;
import com.simple.gateway.orm.entity.route.enums.RouteEnv;
import com.simple.gateway.orm.entity.route.enums.RouteStatus;
import com.simple.gateway.orm.mapper.RouteMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;


@Data
@EqualsAndHashCode(callSuper = true)
public class RouteForm extends BaseEntityForm<Route> {

    /**
     * 应用Id
     */
    Long appId;

    /**
     * 路由协议
     */
    ProtocolType protocol;

    /**
     * 路由方法
     */
    MethodType method;

    /**
     * 路由uri /application/path
     */
    String uri;

    /**
     * 路由路径
     */
    String path;

    /**
     * 路由版本
     */
    String version;

    /**
     * Http源站
     */
    String source;

    /**
     * RPC应用
     */
    String application;

    /**
     * RPC接口
     */
    String interfaceClass;

    /**
     * RPC方法名
     */
    String methodName;

    /**
     * RPC参数
     * {@link com.simple.gateway.orm.entity.route.model.RpcParameter}[]
     * 例如：[{parameterName: "student", parameterType: "com.simple.gateway.dubbodemo.entity.Student", parameterField: "id,name,age"}, {...}]
     */
    String parameters;

    /**
     * 超时时间
     */
    Long timeout;

    /**
     * 路由状态 OK: 正常，DISABLED: 禁用
     */
    RouteStatus status;

    /**
     * 路由环境 DEV: 开发，GRAY: 灰度，PRE: 预发，PROD: 生产
     */
    RouteEnv env;

    /**
     * 路由标签, 以 "," 号分隔
     */
    String tags;

    /**
     * 路由描述
     */
    String description;

    public RouteForm() {
        super(Route.class);
    }

    /**
     * 在这里将 form 中的属性赋值给 Route，并检查参数合理性。
     */
    protected void preCheck(@NonNull Route route) {

        //uri 全局唯一校验:
        //1.创建时验证 2.编辑且修改时验证
        RouteMapper routeMapper = SpringContextUtil.getBean(RouteMapper.class);
        if(this.createMode || (!StringUtil.equals(route.getUri(), uri))) {
            Route dbRoute = routeMapper.selectByUri(uri);

            if(dbRoute != null) {
                throw new BadRequestException("{} 已经存在，请使用其他path", path);
            }
        }

        route.setAppId(appId);
        route.setProtocol(protocol);
        route.setMethod(method);
        route.setUri(uri);
        route.setPath(path);
        route.setVersion(version);
        route.setSource(source);
        route.setApplication(application);
        route.setInterfaceClass(interfaceClass);
        route.setMethodName(methodName);
        route.setParameters(parameters);
        route.setTimeout(timeout);
        route.setStatus(status);
        route.setEnv(env);
        route.setTags(tags);
        route.setDescription(description);

    }

}
