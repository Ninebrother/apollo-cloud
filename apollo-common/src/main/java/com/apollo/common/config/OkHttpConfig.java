package com.apollo.common.config;

import feign.Feign;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by hujl on 2019/11/17.
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class) //SpringBoot自动配置
public class OkHttpConfig {
    // 默认老外留给你彩蛋中文乱码，加上它就 OK
    @Bean
    public Encoder encoder() {
        return new FormEncoder();
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(10, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(10, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(10, TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                .build();
    }
}
