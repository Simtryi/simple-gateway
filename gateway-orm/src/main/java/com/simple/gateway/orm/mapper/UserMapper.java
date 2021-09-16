package com.simple.gateway.orm.mapper;

import com.github.pagehelper.Page;
import com.simple.gateway.orm.base.BaseMapper;
import com.simple.gateway.orm.entity.user.User;
import com.simple.gateway.orm.enums.SortDirection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 当入参中含有 PageNum 和 PageSize 时，会自动分页。
     */
    Page<User> page(
            @Param("pageNum") int pageNum,
            @Param("pageSize") int pageSize,
            @Param("orderCreatedAt") SortDirection orderCreatedAt,
            @Param("orderUpdatedAt") SortDirection orderUpdatedAt,
            @Param("orderLastTime") SortDirection orderLastTime,
            @Param("username") String username,
            @Param("nickname") String nickname,
            @Param("no") String no,
            @Param("department") String department
    );

    /**
     * 根据 nickname 查询 User
     * @param nickname
     * @return
     */
    User selectByNickname(@Param("nickname") String nickname);
}
