package com.sunm.apollo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by zhangyl on 2019/11/19.
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayController {
    public static void main(String[] args) {
        SpringApplication.run(GatewayController.class, args);
    }
}
