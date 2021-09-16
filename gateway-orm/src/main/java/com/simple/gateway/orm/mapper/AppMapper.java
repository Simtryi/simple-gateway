package com.simple.gateway.orm.mapper;

import com.github.pagehelper.Page;
import com.simple.gateway.orm.base.BaseMapper;
import com.simple.gateway.orm.entity.app.App;
import com.simple.gateway.orm.enums.SortDirection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AppMapper extends BaseMapper<App> {

    /**
     * 当入参中含有 pageNum 和 pageSize 时，会自动分页。
     */
    Page<App> page(
            @Param("pageNum") int pageNum,
            @Param("pageSize") int pageSize,
            @Param("orderCreatedAt") SortDirection orderCreatedAt,
            @Param("orderUpdatedAt") SortDirection orderUpdatedAt,
            @Param("keyword") String keyword,
            @Param("name") String name,
            @Param("code") String code
    );

    /**
     * 根据 code 查询 App
     */
    App selectByCode(@Param("code") String code);

}

