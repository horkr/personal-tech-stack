package com.horkr.algorithm.verification;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 卢亮宏
 */
public class NumberVerify {
    public static void main(String[] args) {
        String text = "01";
        Integer integer = Integer.valueOf(text);
        boolean numeric = StringUtils.isNumeric(text);
        System.out.println(numeric);
    }
}
