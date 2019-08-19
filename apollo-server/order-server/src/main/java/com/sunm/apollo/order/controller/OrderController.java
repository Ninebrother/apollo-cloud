package com.sunm.apollo.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangyl on 2019/6/27
 */
@RestController
@RequestMapping("order")
@RefreshScope
public class OrderController {
//    @Value("${env}")
    private String env;

    @RequestMapping("env")
    public String env(){
        return env;
    }
}
