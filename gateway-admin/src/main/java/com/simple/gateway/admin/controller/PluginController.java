package com.simple.gateway.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.simple.gateway.admin.base.BaseController;
import com.simple.gateway.admin.result.WebResult;
import com.simple.gateway.core.service.PluginService;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.enums.SortDirection;
import com.simple.gateway.orm.form.PluginForm;
import com.simple.gateway.orm.mapper.PluginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/plugin")
@CrossOrigin
public class PluginController extends BaseController {

    @Autowired
    PluginService pluginService;

    @Autowired
    PluginMapper pluginMapper;

    @PostMapping(value = "/create")
    public WebResult<Plugin> create(@RequestBody PluginForm pluginForm) {
        Plugin plugin = pluginService.create(pluginForm);
        return success(plugin);
    }

    @RequestMapping(value = "/delete/{id}")
    public WebResult<Void> delete(@PathVariable long id) {
        Plugin plugin = pluginMapper.selectById(id);

        if (plugin != null) {
            pluginMapper.deleteById(id);
            log.info("delete plugin: {}", JSON.toJSONString(plugin));
        }

        return success();
    }

    @PostMapping(value = "/edit")
    public WebResult<Plugin> edit(@RequestBody PluginForm pluginForm) {
        Plugin plugin = pluginService.edit(pluginForm);
        return success(plugin);
    }

    @RequestMapping(value = "/detail/{id}")
    public WebResult<Plugin> detail(@PathVariable long id) {
        Plugin plugin = pluginMapper.selectById(id);
        return success(plugin);
    }


    @RequestMapping(value = "/list")
    public WebResult<Page<Plugin>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) SortDirection orderCreatedAt,
            @RequestParam(required = false) SortDirection orderUpdatedAt
    ) {
        Page<Plugin> page = pluginMapper.page(
                pageNum,
                pageSize,
                orderCreatedAt,
                orderUpdatedAt);

        return success(page);
    }

    /**
     * 根据 路由Id 查找 Plugin
     */
    @RequestMapping(value = "/select/{routeId}")
    public WebResult<Plugin> selectByRouteId(@PathVariable Long routeId) {
        Plugin plugin = pluginMapper.selectByRouteId(routeId);
        return success(plugin);
    }

}
