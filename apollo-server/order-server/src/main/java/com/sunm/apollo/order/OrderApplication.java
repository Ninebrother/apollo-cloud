package com.sunm.apollo.order;

import com.apollo.common.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by zhangyl on 2019/6/27
 */
@SpringBootApplication(scanBasePackages = {"com.sunm.apollo.order", "com.sunm.apollo.account.hystrix"}, scanBasePackageClasses = {SwaggerConfig.class}, exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.sunm.apollo.account")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
