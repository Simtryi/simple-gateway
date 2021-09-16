package com.simple.gateway.core.service.impl;

import com.simple.gateway.core.service.UserService;
import com.simple.gateway.orm.entity.user.User;
import com.simple.gateway.orm.form.UserForm;
import com.simple.gateway.orm.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User create(UserForm userForm) {
        User user = userForm.create();
        userMapper.insert(user);

        //设置 no
        user.setNo("" + user.getId());
        userMapper.update(user);
        return user;
    }

    @Override
    public User edit(UserForm userForm) {
        User user = userForm.update();
        userMapper.update(user);

        return user;
    }

}
