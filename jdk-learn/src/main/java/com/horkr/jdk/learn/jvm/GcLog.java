package com.horkr.jdk.learn.jvm;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 卢亮宏
 */
public class GcLog {
    public static void main(String[] args) {
        List<byte[]> list = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            byte[] bytes = new byte[1024 * 1024];
            list.add(bytes);
        }
    }
}
