package com.horkr.jdk.learn.network.io.nio.selector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class SelectorDemo {
    private static final Logger log = LogManager.getLogger(SelectorDemo.class);


    /**
     * 一个selector管理多个 ServerSocketChannel 以及分别对应ServerSocketChannel连接的客户端
     */
    private static void selector() throws Exception {
        int[] ports = new int[5];
        ports[0] = 11000;
        ports[1] = 11001;
        ports[2] = 11002;
        ports[3] = 11003;
        ports[4] = 11004;
        Selector selector = Selector.open();
        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 配置是否阻塞等待客户端连接
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(ports[i]));
            // 将serverSocketChannel注册到selector中，并选择接收连接的key。当客户端连过来时，服务器端就会建立连接且可以拿到链接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.err.println("监听端口：" + ports[i]);
        }

        while (true) {
            // 返回发生事件的个数，多个通道注册到selector中，所以可能会同时有多个
            int selectNum = selector.select();
            System.err.println("selectNum:" + selectNum);

            // 发生事件的set集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    iterator.remove();
                    System.err.println("获取到客户端连接：" + socketChannel);
                } else if (selectionKey.isReadable()) {
                    System.err.println("触发读。。。");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(512);
                    // 把channel中的所有字节读到buffer中
                    socketChannel.read(buffer);
                    buffer.flip();
                    Charset charset = Charset.forName("utf-8");
                    String message = String.valueOf(charset.decode(buffer).array());
                    System.err.println("接收到消息:" + message);
                    buffer.clear();
                    // 把buffer中的所有字节写到channel中
                    socketChannel.write(buffer);
                    System.err.println("当前事件已结束");
                    iterator.remove();
                }
            }
        }
    }


    private static void singleServer() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(10002));
        //配置为非阻塞的
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();


        //先将serverSocketChannel的接收客户端连接时间注册到selector中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //selector的select系列方法是获取注册到当前selector上且发生相应触发事件的channel
        while (true) {
            if (selector.select(1000) == 0) {
                log.info("当前服务端等待1s无连接...");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 当有客户端连接到 serverSocketChannel
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    log.info("客户端[{}]已连接", socketChannel.hashCode());
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } else if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    // 将channel里的内容读到buffer
                    channel.read(byteBuffer);
                    log.info("客户端[{}]：{}", channel.hashCode(), new String(byteBuffer.array()));
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws Exception{
        singleServer();
    }

}
