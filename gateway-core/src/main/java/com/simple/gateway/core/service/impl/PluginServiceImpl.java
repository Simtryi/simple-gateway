package com.simple.gateway.core.service.impl;

import com.simple.gateway.core.service.PluginService;
import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.form.PluginForm;
import com.simple.gateway.orm.mapper.PluginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PluginServiceImpl implements PluginService {

    @Autowired
    PluginMapper pluginMapper;

    @Override
    public Plugin create(PluginForm pluginForm) {
        Plugin plugin = pluginForm.create();
        pluginMapper.insert(plugin);

        return plugin;
    }

    @Override
    public Plugin edit(PluginForm pluginForm) {
        Plugin plugin = pluginForm.update();
        pluginMapper.update(plugin);

        return plugin;
    }
    
}
