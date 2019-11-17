package com.sunm.apollo.account;

import com.sunm.apollo.account.hystrix.AccountFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangyl on 2019/7/27.
 */
@FeignClient(value = "account", path = "account", fallback = AccountFeignHystrix.class)
public interface AccountFeign {
    /**
     * 测试重试时间
     *
     * @param mills
     * @mills
     */
    @GetMapping(value = "timeout")
    String timeout(@RequestParam("mills") int mills);
}
