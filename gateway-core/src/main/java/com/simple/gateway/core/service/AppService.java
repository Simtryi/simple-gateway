package com.simple.gateway.core.service;

import com.simple.gateway.orm.entity.app.App;
import com.simple.gateway.orm.form.AppForm;

public interface AppService {

    App create(AppForm appForm);

    App edit(AppForm appForm);

}
