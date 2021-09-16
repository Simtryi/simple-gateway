package com.simple.gateway.orm.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BaseMapper<T> {

    /**
     * 插入一个对象，返回影响的条数
     */
    int insert(T t);

    /**
     * 批量插入对象
     */
    int insertBatch(@Param("list") List<T> list);

    /**
     * 按id删除
     */
    int deleteById(Long id);

    /**
     * 批量删除对象
     */
    int deleteByIds(@Param("list") List<Long> list);

    /**
     * 更新，返回影响的条数
     */
    int update(T fsEvent);

    /**
     * 按id查询
     */
    T selectById(Long id);

    /**
     * 批量查询对象
     */
    List<T> selectByIds(@Param("list") List<Long> list);

}
