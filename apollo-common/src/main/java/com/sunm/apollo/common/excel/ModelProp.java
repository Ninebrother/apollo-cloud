package com.sunm.apollo.common.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangyl
 * @Date 2020/2/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelProp {
    String name();

    int colIndex() default -1;

    boolean nullable() default true;

    String interfaceXmlName() default "";
}
