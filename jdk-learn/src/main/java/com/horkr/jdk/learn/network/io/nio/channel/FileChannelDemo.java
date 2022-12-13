package com.horkr.jdk.learn.network.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {
    private static void read() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("text.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        channel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.err.println((char) byteBuffer.get());
        }
        fileInputStream.close();
    }

    private static void write() throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("text1.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] msg = "ni hao".getBytes();
        byteBuffer.put(msg);
        byteBuffer.flip();
        // 这里应该理解为，channel要被buffer中的数据写入，是写到channel中，所有buffer是被读的，在这之前buffer是被写的，所以要反转
        channel.write(byteBuffer);
        fileOutputStream.close();
    }
    public static void main(String[] args) throws Exception{
        read();
    }
}
