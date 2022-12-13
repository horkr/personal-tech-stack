package com.horkr.jdk.learn.network.io.nio.nio_group_chat;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Executors;

public class NioChatClient {

    public static void startClient() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        // 为客户端注册链接事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8989));

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(selectionKey -> {
                try {
                    if (selectionKey.isConnectable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                            ByteBuffer buffer = ByteBuffer.allocate(512);
                            buffer.put("connect successful".getBytes());
                            buffer.flip();
                            channel.write(buffer);
                            Executors.newSingleThreadExecutor().submit(() -> {
                                while (true) {
                                    buffer.clear();
                                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                    String s = bufferedReader.readLine();
                                    buffer.put(s.getBytes());
                                    buffer.flip();
                                    channel.write(buffer);
                                }
                            });
                        }
                        channel.register(selector, SelectionKey.OP_READ);

                    } else if (selectionKey.isReadable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();

                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        int count = client.read(readBuffer);

                        if (count > 0) {
                            String receivedMessage = new String(readBuffer.array(), 0, count);
                            System.out.println(receivedMessage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            selectionKeys.clear();
        }
    }

    public static void main(String[] args) throws Exception {
        startClient();
    }


}
