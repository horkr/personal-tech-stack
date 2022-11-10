//package com.horkr.jdk.learn.concurrency.cas;
//
//import cn.hutool.core.util.ReflectUtil;
//import sun.misc.Unsafe;
//
///**
// * @author 卢亮宏
// */
//public class UnSafeFactory {
//
//    public static final Unsafe instance;
//
//    static {
//        instance = getInstance();
//    }
//
//
//    private static Unsafe getInstance() {
//        return (Unsafe) ReflectUtil.getFieldValue(Unsafe.class, "theUnsafe");
//    }
//
//}
