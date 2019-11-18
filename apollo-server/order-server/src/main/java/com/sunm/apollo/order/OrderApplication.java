package com.sunm.apollo.order;

import com.sunm.apollo.common.config.JsonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by zhangyl on 2019/6/27
 * basePackageClasses属性会去扫描类所在包下的所有组件，而不是指定某个组件！
 */
@SpringBootApplication(scanBasePackages = {"com.sunm.apollo.order", "com.sunm.apollo.account.hystrix"},
        scanBasePackageClasses = {JsonConfig.class}, exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.sunm.apollo.account")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
