package com.simple.gateway.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.simple.gateway.core.service.UserService;
import com.simple.gateway.orm.entity.user.User;
import com.simple.gateway.orm.enums.SortDirection;
import com.simple.gateway.orm.form.UserForm;
import com.simple.gateway.orm.mapper.UserMapper;
import com.simple.gateway.admin.base.BaseController;
import com.simple.gateway.admin.result.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @PostMapping(value = "/create")
    public WebResult<User> create(@RequestBody UserForm userForm) {
        User user = userService.create(userForm);
        return success(user);
    }



    @RequestMapping(value = "/delete/{id}")
    public WebResult<Void> delete(@PathVariable long id) {
        User user = userMapper.selectById(id);

        if (user != null) {
            userMapper.deleteById(id);
            log.info("delete user: {}", JSON.toJSONString(user));
        }

        return success();
    }

    @PostMapping(value = "/edit")
    public WebResult<User> edit(@RequestBody UserForm userForm) {
        User user = userService.edit(userForm);
        return success(user);
    }

    @RequestMapping(value = "/detail/{id}")
    public WebResult<User> detail(@PathVariable long id) {
        User user = userMapper.selectById(id);
        return success(user);
    }


    @RequestMapping(value = "/list")
    public WebResult<Page<User>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) SortDirection orderCreatedAt,
            @RequestParam(required = false) SortDirection orderUpdatedAt,
            @RequestParam(required = false) SortDirection orderLastTime,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String no,
            @RequestParam(required = false) String department
    ) {
        Page<User> page = userMapper.page(
                pageNum,
                pageSize,
                orderCreatedAt,
                orderUpdatedAt,
                orderLastTime,
                username,
                nickname,
                no,
                department);

        return success(page);
    }
    
}
