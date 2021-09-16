package com.simple.gateway.orm.mapper;

import com.github.pagehelper.Page;
import com.simple.gateway.orm.base.BaseMapper;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.enums.SortDirection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PluginMapper extends BaseMapper<Plugin> {

    /**
     * 当入参中含有 pageNum 和 pageSize 时，会自动分页。
     */
    Page<Plugin> page(
            @Param("pageNum") int pageNum,
            @Param("pageSize") int pageSize,
            @Param("orderCreatedAt") SortDirection orderCreatedAt,
            @Param("orderUpdatedAt") SortDirection orderUpdatedAt
    );

    /**
     * 根据 路由Id 查找 Plugin
     */
    Plugin selectByRouteId(@Param("routeId") Long routeId);

}
