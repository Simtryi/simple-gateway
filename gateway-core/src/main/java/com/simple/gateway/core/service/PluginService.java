package com.simple.gateway.core.service;

import com.simple.gateway.orm.entity.plugin.Plugin;
import com.simple.gateway.orm.form.PluginForm;

public interface PluginService {

    Plugin create(PluginForm pluginForm);

    Plugin edit(PluginForm pluginForm);

}
