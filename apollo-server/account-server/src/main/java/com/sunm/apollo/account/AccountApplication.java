package com.sunm.apollo.account;

import com.sunm.apollo.common.config.JsonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by zhangyl on 2019/7/31.
 */
@SpringBootApplication(scanBasePackageClasses = JsonConfig.class, exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
