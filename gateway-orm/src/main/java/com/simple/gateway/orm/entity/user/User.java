package com.simple.gateway.orm.entity.user;

import com.simple.gateway.orm.base.BaseEntity;
import com.simple.gateway.orm.entity.user.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 花名，全局唯一
     */
    private String nickname;

    /**
     * 工号，全局唯一
     */
    private String no = "0";

    /**
     * 部门
     */
    private String department;

    /**
     * 描述
     */
    private String description;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 上次活跃时间
     */
    private Date lastTime = new Date();

    /**
     * 用户状态
     */
    private UserStatus status = UserStatus.OK;

}
