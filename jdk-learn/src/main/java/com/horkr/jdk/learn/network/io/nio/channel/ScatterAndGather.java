package com.horkr.jdk.learn.network.io.nio.channel;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * buffer的分散与聚集，即读写操作时操作的是buffer数组
 */
public class ScatterAndGather {
    private static void buffers() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));
        int messageLength = 2 + 3 + 4;
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);
        SocketChannel socketChannel = serverSocketChannel.accept();
        while (true){
            int readLength = 0;
            while (readLength < messageLength) {
                // 从buffer中读取数据，即写入到channel中
                long read = socketChannel.read(buffers);
                readLength += read;
                System.err.println("readlength：" + readLength);
                Stream.of(buffers).forEach(buffer -> {
                    System.err.println("position:" + buffer.position() + "  " + "limit:" + buffer.limit() + "  ");
                });
            }
            Stream.of(buffers).forEach(Buffer::flip);
            int writeLength = 0;
            while (writeLength < messageLength) {
                // 从buffer中读取数据，即从buffer中写入channel
                long write = socketChannel.write(buffers);
                writeLength += write;
            }
            Arrays.asList(buffers).forEach(Buffer::clear);
        }

    }

    public static void main(String[] args) throws Exception {
        buffers();
    }
}
