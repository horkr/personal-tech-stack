package com.horkr.jdk.learn.network.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DirectBufferDemo {
    private static void directByteByffer() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("text.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("text2.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);
        while (true) {
            //clear方法只是将position,limit等置为初始值，并没有删除元素，只是再put时会覆盖，像是被清除了
            byteBuffer.clear();
            int read = inputStreamChannel.read(byteBuffer);
            System.err.println("read：" + read);
            if (-1 == read) {
                break;
            }
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }
        inputStreamChannel.close();
        outputStreamChannel.close();
    }
}
