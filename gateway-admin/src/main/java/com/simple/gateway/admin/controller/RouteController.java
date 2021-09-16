package com.simple.gateway.admin.controller;

import com.github.pagehelper.Page;
import com.simple.gateway.admin.base.BaseController;
import com.simple.gateway.admin.result.WebResult;
import com.simple.gateway.core.service.RouteService;
import com.simple.gateway.orm.entity.route.Route;
import com.simple.gateway.orm.entity.route.model.RouteRequest;
import com.simple.gateway.orm.entity.route.model.RouteResponse;
import com.simple.gateway.orm.enums.SortDirection;
import com.simple.gateway.orm.form.RouteForm;
import com.simple.gateway.orm.mapper.RouteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/route")
@CrossOrigin
public class RouteController extends BaseController {

    @Autowired
    RouteService routeService;

    @Autowired
    RouteMapper routeMapper;

    @PostMapping(value = "/create")
    public WebResult<Route> create(@RequestBody RouteForm routeForm) {
        Route route = routeService.create(routeForm);
        return success(route);
    }

    @RequestMapping(value = "/delete/{id}")
    public WebResult<Void> delete(@PathVariable long id) {
        routeService.delete(id);
        return success();
    }

    @PostMapping(value = "/edit")
    public WebResult<Route> edit(@RequestBody RouteForm routeForm) {
        Route route = routeService.edit(routeForm);
        return success(route);
    }

    @RequestMapping(value = "/detail/{id}")
    public WebResult<Route> detail(@PathVariable long id) {
        Route route = routeMapper.selectById(id);
        return success(route);
    }


    @RequestMapping(value = "/list")
    public WebResult<Page<Route>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) SortDirection orderCreatedAt,
            @RequestParam(required = false) SortDirection orderUpdatedAt,
            @RequestParam Long appId,
            @RequestParam(required = false) String keyword
    ) {
        Page<Route> page = routeMapper.page(
                pageNum,
                pageSize,
                orderCreatedAt,
                orderUpdatedAt,
                appId,
                keyword);

        return success(page);
    }


    /**
     * 路由查询
     */
    @PostMapping(value = "/query")
    public WebResult<RouteResponse> query(@RequestBody RouteRequest request) {
        RouteResponse response = routeService.query(request);
        return success(response);
    }

}

