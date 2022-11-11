package com.horkr.jdk.learn.jvm.directmemery;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Demo {
    public static void main(String[] args) throws IOException {
        int _1Gb = 1024 * 1024 * 1024;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(_1Gb);
        System.err.println("分配完毕");
        System.in.read();
        System.err.println("开始释放");
        byteBuffer = null;
        System.gc();
        System.in.read();
    }
}
