package com.simple.gateway.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.simple.gateway.core.service.AppService;
import com.simple.gateway.orm.entity.app.App;
import com.simple.gateway.orm.enums.SortDirection;
import com.simple.gateway.orm.form.AppForm;
import com.simple.gateway.orm.mapper.AppMapper;
import com.simple.gateway.admin.base.BaseController;
import com.simple.gateway.admin.result.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/app")
@CrossOrigin
public class AppController extends BaseController {

    @Autowired
    AppService appService;

    @Autowired
    AppMapper appMapper;

    @PostMapping(value = "/create")
    public WebResult<App> create(@RequestBody AppForm appForm) {
        App app = appService.create(appForm);
        return success(app);
    }

    @RequestMapping(value = "/delete/{id}")
    public WebResult<Void> delete(@PathVariable long id) {
        App app = appMapper.selectById(id);

        if (app != null) {
            appMapper.deleteById(id);
            log.info("delete app: {}", JSON.toJSONString(app));
        }

        return success();
    }

    @PostMapping(value = "/edit")
    public WebResult<App> edit(@RequestBody AppForm appForm) {
        App app = appService.edit(appForm);
        return success(app);
    }

    @RequestMapping(value = "/detail/{id}")
    public WebResult<App> detail(@PathVariable long id) {
        App app = appMapper.selectById(id);
        return success(app);
    }


    @RequestMapping(value = "/list")
    public WebResult<Page<App>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) SortDirection orderCreatedAt,
            @RequestParam(required = false) SortDirection orderUpdatedAt,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code
    ) {
        Page<App> page = appMapper.page(
                pageNum,
                pageSize,
                orderCreatedAt,
                orderUpdatedAt,
                keyword,
                name,
                code);

        return success(page);
    }

}
