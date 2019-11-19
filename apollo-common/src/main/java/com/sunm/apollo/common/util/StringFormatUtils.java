package com.sunm.apollo.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zhangyl on 2019/11/19.
 * 以"#"补位
 *  整数部分多了:不会截断,但是排在有效位最前面的0会被删除
 *  整数部分少了:不作处理
 *  小数部分多了:截断,建议指定RoundingMode,默认为RoundingMode.HALF_EVEN
 *  小数部分少了:不作处理
 * 以"0"补位
 *  整数部分多了:不会截断,排在有效位前面的0也不会被删除
 *  整数部分少了:补0
 *  小数部分多了:截断,建议指定RoundingMode,默认为RoundingMode.HALF_EVEN
 *  小数部分少了:补0
 * ————————————————
 */
public class StringFormatUtils {
    private static final DecimalFormat decimalFormat;

    static {
        decimalFormat = new DecimalFormat();
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    private static String format(String pattern, BigDecimal decimal) {
        decimalFormat.applyPattern(pattern);
        return decimalFormat.format(decimal);
    }

    public static void main(String[] args) {
        System.out.println(format("####.##", new BigDecimal("12")));// 12
        System.out.println(format("0000.00", new BigDecimal("12")));// 0012.00
        System.out.println(format("#00.00", new BigDecimal("13112.00")));// 12.00
        System.out.println(format("000.00", new BigDecimal("0012.00")));// 012.00
        System.out.println(format("#.00", new BigDecimal("0012.00")));// 12.00
        System.out.println(format("0.00", new BigDecimal("0012.00")));// 12.00

    }

}
