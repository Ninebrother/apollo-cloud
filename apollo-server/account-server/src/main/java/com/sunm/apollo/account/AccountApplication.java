package com.sunm.apollo.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by zhangyl on 2019/7/31.
 */
@SpringBootApplication(scanBasePackages = {"com.sunm.apollo.account", "com.sunm.apollo.common.config"})
@EnableEurekaClient
@MapperScan("com.sunm.apollo.account.mapper")
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
