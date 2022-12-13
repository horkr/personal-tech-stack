package com.horkr.jdk.learn.network.io.nio.selector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientDemo {
    private static final Logger log = LogManager.getLogger(ClientDemo.class);

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 10002))) {
            while (!socketChannel.finishConnect()) {
                log.info("客户端连接中...");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap("hello world".getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
