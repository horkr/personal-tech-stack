package com.horkr.jdk.learn.network.io.nio.channel;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBufferDemo {

    /**
     * MappedByteBuffer 映射文件的Buffer，对buffer进行的修改操作系统会同步到文件中。底层数组开辟在堆外内存
     * 修改后可到文件系统中看改变，idea没有同步过去
     *
     * @throws Exception
     */
    private static void mappedByteBuffer() throws Exception {
        // 参数为文件名和读写方式
        RandomAccessFile randomAccessFile = new RandomAccessFile("text3.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        // 参数为读写类型，起始位置，size.即将文件中从0开始的5个字节映射到内存，可读写的也是这个范围。不包含5这个位置
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) '2');
        mappedByteBuffer.put(4, (byte) '5');
        randomAccessFile.close();
    }
}
