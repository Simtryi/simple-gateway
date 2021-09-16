package com.simple.gateway.orm.base;

import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdAt = new Date();

    /**
     * 修改时间
     */
    private Date updatedAt = new Date();

}
