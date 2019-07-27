package com.sunm.apollo.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by zhangyl on 2019/7/27.
 */
@FeignClient(value = "account-server")
public interface AccountFeign {
    /**
     * 获取账户金额
     *
     * @param accountId
     * @return
     */
    @GetMapping(value = "/{accountId}")
    int getAccountId(@PathVariable("accountId") String accountId);
}
