package com.simple.gateway.core.service;

import com.simple.gateway.orm.entity.user.User;
import com.simple.gateway.orm.form.UserForm;

public interface UserService {

    User create(UserForm userForm);

    User edit(UserForm userForm);

}
