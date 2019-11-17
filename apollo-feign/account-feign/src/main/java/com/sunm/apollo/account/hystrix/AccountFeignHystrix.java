package com.sunm.apollo.account.hystrix;

import com.sunm.apollo.account.AccountFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by hujl on 2019/11/17.
 */
@Component
public class AccountFeignHystrix implements AccountFeign {
    public static final Logger logger = LoggerFactory.getLogger(AccountFeignHystrix.class);

    @Override
    public String timeout(int mills) {
        logger.warn("熔断");
        return "熔断了";
    }
}
