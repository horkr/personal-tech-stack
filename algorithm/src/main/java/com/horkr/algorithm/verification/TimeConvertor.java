package com.horkr.algorithm.verification;

import java.util.concurrent.TimeUnit;

/**
 * @author 卢亮宏
 */
public class TimeConvertor {
    public static void main(String[] args) {
        long convert = TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES);
        long l = System.currentTimeMillis();
        System.out.println(convert);
        System.out.println(l);
        System.out.println(l-convert);
    }
}
