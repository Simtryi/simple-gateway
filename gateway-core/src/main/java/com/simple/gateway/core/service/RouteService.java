package com.simple.gateway.core.service;

import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.entity.route.model.RouteRequest;
import com.simple.gateway.orm.entity.route.model.RouteResponse;
import com.simple.gateway.orm.form.RouteForm;

public interface RouteService {

    Route create(RouteForm routeForm);

    Route edit(RouteForm routeForm);

    void delete(Long id);

    RouteResponse query(RouteRequest request);

}
