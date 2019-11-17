package com.sunm.apollo.order.controller;

import com.sunm.apollo.account.AccountFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangyl on 2019/6/27
 */
@RestController
@RequestMapping("order")
@Api("测试重拾")
public class OrderController {
    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private AccountFeign accountFeign;

    @GetMapping("/timeOut")
    @ApiOperation("重连")
    public String timeOut(@RequestParam int mills) {
        logger.info("开始调用");
        return accountFeign.timeout(mills);
    }

}
