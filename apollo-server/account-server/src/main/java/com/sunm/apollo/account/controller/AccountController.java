package com.sunm.apollo.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hujl on 2019/11/17.
 */
@RestController
@RequestMapping("account")
public class AccountController {

    public static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Value("${server.port}")
    private int port;

    /**
     * 测试feign  hystrix超时时间
     *
     * @param mills
     * @return
     */
    @GetMapping("timeout")
    public String timeout(@RequestParam("mills") int mills) {
        logger.info("[client服务-{}] [timeOut方法]收到请求,阻塞{}ms", port, mills);
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("[client服务-{}] [timeOut]返回请求", port);
        return String.format("client服务-%s 请求ok!!!", port);
    }
}
