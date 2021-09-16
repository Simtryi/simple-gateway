package com.simple.gateway.orm.entity.app;

import com.simple.gateway.orm.base.BaseEntity;
import com.simple.gateway.orm.entity.app.enums.AppStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 应用
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class App extends BaseEntity {

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用code，全局唯一
     */
    private String code;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用状态
     */
    private AppStatus status;

    /**
     * 负责人花名
     */
    private String ownerNickname;

    /**
     * 负责人工号
     */
    private String ownerNo;

    /**
     * 更新人花名
     */
    private String updatedOwnerNickname;

    /**
     * 更新人工号
     */
    private String updatedOwnerNo;

}
