package com.horkr.jdk.learn.network.io.nio.nio_group_chat;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * 创建一个聊天室，类似于群，一个客户端发送消息过来，服务端再转发到各个客户端
 */
public class NioChatServer {
    private static HashMap<String, SocketChannel> clientMap = new HashMap<>();
    private static int num = 1;

    private static ServerSocketChannel createServerSocketChannel(String ip, Integer port) throws Exception {
        if (isNull(ip) || isNull(port)) {
            System.err.println("参数不能为空");
        }
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(ip, port));
        return serverSocketChannel;
    }

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = createServerSocketChannel("localhost", 8989);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int select = selector.select();
            System.out.println("发生事件个数:" + select);
            // 遍历各个事件，各个事件是对于当前 selector 的，如有发生接收连接的事件，读取信息的事件
            selector.selectedKeys().forEach(selectionKey -> {
                SocketChannel socketChannel;
                try {
                    // 连接事件
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel subServerSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        socketChannel = subServerSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 注册读监听事件到selector中，并为连接到server的client标号
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        clientMap.put("client-" + num, socketChannel);
                        num++;
                        // 读取事件
                    } else if (selectionKey.isReadable()) {
                        socketChannel = (SocketChannel) selectionKey.channel();
                        String clientKey = null;
                        // 找到客户端的名字
                        for (Map.Entry entry : clientMap.entrySet()) {
                            if (entry.getValue() == socketChannel) {
                                clientKey = (String) entry.getKey();
                            }
                        }
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 从socketChannel读出数据，即将socketChannel中的数据写入到buffer中，buffer充当着被写的角色，如果再想要读，则需要反转
                        socketChannel.read(buffer);
                        buffer.flip();
                        Charset charset = Charset.forName("utf-8");
                        String message = String.valueOf(charset.decode(buffer).array());
                        System.err.println(clientKey + ":" + message);
                        for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                            if (!entry.getKey().equals(clientKey)) {
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                byteBuffer.put((clientKey + ":" + message).getBytes());
                                SocketChannel clientChannel = entry.getValue();
                                byteBuffer.flip();
                                // 将buffer写入到clientChannel
                                clientChannel.write(byteBuffer);
                            } else {
                                SocketChannel clientChannel =  entry.getValue();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                byteBuffer.put(("me:" + message).getBytes());
                                byteBuffer.flip();
                                clientChannel.write(byteBuffer);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            selector.selectedKeys().clear();
        }
    }
}
