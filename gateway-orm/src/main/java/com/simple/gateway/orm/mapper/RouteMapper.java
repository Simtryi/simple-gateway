package com.simple.gateway.orm.mapper;

import com.github.pagehelper.Page;
import com.simple.gateway.orm.base.BaseMapper;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.enums.SortDirection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RouteMapper extends BaseMapper<Route> {

    /**
     * 当入参中含有 pageNum 和 pageSize 时，会自动分页。
     */
    Page<Route> page(
            @Param("pageNum") int pageNum,
            @Param("pageSize") int pageSize,
            @Param("orderCreatedAt") SortDirection orderCreatedAt,
            @Param("orderUpdatedAt") SortDirection orderUpdatedAt,
            @Param("appId") Long appId,
            @Param("keyword") String keyword
    );

    /**
     * 根据 uri 查找 Route
     */
    Route selectByUri(@Param("uri") String uri);

}
