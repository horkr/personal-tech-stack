package com.horkr.jdk.learn.network.io.reactor;

import cn.hutool.core.io.BufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 卢亮宏
 */
public class NioThread extends Thread {
    private final Logger log = LoggerFactory.getLogger(NioThread.class);

    /**
     * 持有的selector
     */
    private Selector selector = null;

    /**
     * 所属的threadGroup
     */
    private NioThreadGroup threadGroup;

    /**
     * 持有的channel，可以理解为创建的ServerChannel或者accept的SocketChannel分配给了当前线程处理
     */
    private final LinkedBlockingQueue<Channel> channelQueue = new LinkedBlockingQueue<>();

    public NioThread(NioThreadGroup threadGroup) {
        try {
            this.selector = Selector.open();
            this.threadGroup = threadGroup;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 添加管理的channel
     *
     * @param channel channel
     */
    public void addChannel(Channel channel) {
        try {
            channelQueue.put(channel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取持有的selector
     */
    public Selector selector() {
        return this.selector;
    }

    @Override
    public void run() {
        while (true) {
            int num = 0;
            try {
                log.info("等待事件发生---------------------------------------");
                num = selector.select();
                log.info("事件发生---------------------------------------");
                if (num > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    // 处理发生的事件
                    processSelectionKeys(selectionKeys);
                }
                // 处理分配到当前线程的channel
                processTask();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 处理分配到当前线程的channel
     */
    private void processTask() {
        if (this.channelQueue.isEmpty()) {
            return;
        }
        try {
            Channel channel = channelQueue.take();
            if (channel instanceof ServerSocketChannel) {
                ((ServerSocketChannel) channel).register(selector, SelectionKey.OP_ACCEPT);
                log.info("channel accept注册到当前线程");
            } else if (channel instanceof SocketChannel) {
                ((SocketChannel) channel).register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                log.info("channel read注册到当前线程");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理发生的事件
     *
     * @param selectionKeys selectionKeys
     */
    private void processSelectionKeys(Set<SelectionKey> selectionKeys) {
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            // 为什么要remove？
            iterator.remove();
            if (selectionKey.isAcceptable()) {
                processAccept(selectionKey);
            } else if (selectionKey.isReadable()) {
                processReadable(selectionKey);
            } else {
                log.info("不支持处理此类型：{}", selectionKey.interestOps());
            }
        }
    }

    /**
     * 处理读事件
     *
     * @param selectionKey selectionKey
     */
    private void processReadable(SelectionKey selectionKey) {
        log.info("发生读事件");
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // 为啥要clear
        buffer.clear();
        // 为啥要loop
        while (true) {
            try {
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    String msg = BufferUtil.readStr(buffer, StandardCharsets.UTF_8);
                    log.info("client {}：{}", channel.getRemoteAddress(), msg);
                    // 将消息再发回去，为啥要loop
                    while (buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                    buffer.clear();
                } else if (read == 0) {
                    log.info("client {} 发送了空消息", channel.getRemoteAddress());
                    break;
                } else {
                    log.info("client {} 断开连接", channel.getRemoteAddress());
                    // epoll不再关注此fd
                    selectionKey.cancel();
                    channel.close();
                    break;
                }
            } catch (IOException e) {
                log.error("处理读事件时出错",e);
            }
        }

    }

    /**
     * 处理accept事件
     *
     * @param selectionKey selectionKey
     */
    private void processAccept(SelectionKey selectionKey) {
        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel socketChannel = channel.accept();
            socketChannel.configureBlocking(false);
            NioThread nioThread = threadGroup.allocateNioThread();
            nioThread.addChannel(socketChannel);
            nioThread.selector().wakeup();
            log.info("发生accept事件,client {} 连接,此客户端由线程{}管理", socketChannel.getRemoteAddress(), nioThread.getName());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
