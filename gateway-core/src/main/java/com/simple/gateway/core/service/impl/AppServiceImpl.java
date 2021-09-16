package com.simple.gateway.core.service.impl;

import com.simple.gateway.core.service.AppService;
import com.simple.gateway.orm.entity.app.App;
import com.simple.gateway.orm.form.AppForm;
import com.simple.gateway.orm.mapper.AppMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppMapper appMapper;

    @Override
    public App create(AppForm appForm) {
        App app = appForm.create();
        appMapper.insert(app);

        return app;
    }

    @Override
    public App edit(AppForm appForm) {
        App app = appForm.update();
        appMapper.update(app);

        return app;
    }

}
