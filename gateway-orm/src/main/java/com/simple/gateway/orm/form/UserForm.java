package com.simple.gateway.orm.form;

import com.simple.gateway.common.exception.BadRequestException;
import com.simple.gateway.common.util.SpringContextUtil;
import com.simple.gateway.common.util.StringUtil;
import com.simple.gateway.orm.base.BaseEntityForm;
import com.simple.gateway.orm.entity.user.User;
import com.simple.gateway.orm.entity.user.enums.UserStatus;
import com.simple.gateway.orm.mapper.UserMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserForm extends BaseEntityForm<User> {

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

    public UserForm() {
        super(User.class);
    }

    /**
     * 在这里将 form 中的属性赋值给 User，并检查参数合理性。
     */
    protected void preCheck(@NonNull User user) {

        //nickname 全局唯一校验:
        //1.创建时验证 2.编辑且修改时验证
        UserMapper userMapper = SpringContextUtil.getBean(UserMapper.class);
        if(this.createMode || (!StringUtil.equals(user.getNickname(), nickname))) {
            User dbUser = userMapper.selectByNickname(nickname);

            if(dbUser != null) {
                throw new BadRequestException("{} 已经存在，请使用其他花名", nickname);
            }
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setDepartment(department);
        user.setDescription(description);
        user.setAvatar(avatar);
        user.setLastTime(lastTime);
        user.setStatus(status);

    }

}
