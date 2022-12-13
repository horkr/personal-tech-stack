package com.horkr.jdk.learn.network.io.nio.buffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @description: Buffer其实就是操作一个数组和这个数组的一些索引。可将普通buffer转为只读，但不能转回来
 * @create: 2019-08-06 11:13
 **/

public class Demo {

    private static Logger log = LogManager.getLogger(Demo.class);

    private static void logBufferIndex(Buffer buffer) {
        log.info("limit:{},position:{}",buffer.limit(),buffer.position());
    }

    public static void practice() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }
        logBufferIndex(intBuffer);
        intBuffer.flip();
        logBufferIndex(intBuffer);
        while (intBuffer.hasRemaining()) {
            logBufferIndex(intBuffer);
            intBuffer.get();
        }
    }

    private static void practice1() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putChar('你');
        byteBuffer.putInt(3);
        byteBuffer.putLong(5L);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.err.println(byteBuffer.getChar());
            System.err.println(byteBuffer.getInt());
            System.err.println(byteBuffer.getLong());
        }
    }

    private static void practice2() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        buffer.position(2);
        buffer.limit(6);
        ByteBuffer slice = buffer.slice();
        // slice 会截取[2-6)前闭后开区间的值，作为一个副本，底层数据是一份，slice变化对应buffer位置也会变化
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 2;
            slice.put(b);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()) {
            System.err.println(buffer.get());
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        practice();
    }
}
