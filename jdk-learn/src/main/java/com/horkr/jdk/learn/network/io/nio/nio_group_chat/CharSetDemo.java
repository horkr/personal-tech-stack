package com.horkr.jdk.learn.network.io.nio.nio_group_chat;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 彻底解决编解码问题
 */
public class CharSetDemo {
    public static void main(String[] args) throws Exception {
        String input = "in.txt";
        String output = "out.txt";
        RandomAccessFile infile = new RandomAccessFile(input, "r");
        RandomAccessFile outfile = new RandomAccessFile(output, "rw");
        long length = new File(input).length();
        FileChannel inchannel = infile.getChannel();
        FileChannel outchannel = outfile.getChannel();
        MappedByteBuffer buffer = inchannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
        System.err.println("------------------系统可用字符集--------------------");
        Charset.availableCharsets().forEach((k,v)->{
            System.err.println(k+","+v);
        });
        System.err.println("---------------------------------------------------");


        Charset charset = Charset.forName("iso-8859-1");
        // 将字节数组转为字符串
        CharsetDecoder decoder = charset.newDecoder();
        // 将字符串转换为字节数组
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = decoder.decode(buffer);
        ByteBuffer byteBuffer = encoder.encode(charBuffer);
        outchannel.write(byteBuffer);
        infile.close();
        outfile.close();

    }

}
