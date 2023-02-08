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
    private NioThreadGroup nioThreadGroup;

    /**
     * 持有的channel，可以理解为创建的ServerChannel或者accept的SocketChannel分配给了当前线程处理，后续负责对队列中的channel向selector进行注册‘
     * @see NioThread#processTask()
     */
    private final LinkedBlockingQueue<Channel> channelQueue = new LinkedBlockingQueue<>();

    public NioThread(NioThreadGroup threadGroup) {
        try {
            this.selector = Selector.open();
            this.nioThreadGroup = threadGroup;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public NioThreadGroup getNioThreadGroup() {
        return nioThreadGroup;
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
                    log.info("selectionKeys set hashcode:{}", selectionKeys.hashCode());
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
            // 为什么要remove? 因为selectionKey总是一个对象。selector.selectedKeys()不会产生新的对象，如果不remove，下次会重新处理
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
        // 为啥要clear？clear是重新归位limit和position的位置，可能之前对这个attach的buffer有过操作，导致结果不对！这里要把channel的数据写入到buffer中，clear后相当于一个新的buffer
        buffer.clear();
        // 为啥要loop,因为 channel.read(buffer)会将数据从channel中写入到buffer，但是buffer大小个字节，如果channel里的数据大于buffer的大小，则要重新循环写.read变量为channel写入到buffer数据的size
        while (true) {
            try {
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    String msg = BufferUtil.readStr(buffer, StandardCharsets.UTF_8);
                    log.info("client {}：{}", channel.getRemoteAddress(), msg);
                    // 将消息再发回去，为啥要loop，因为要保证一直到buffer的数据写完
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
                log.error("处理读事件时出错", e);
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
            NioThread nioThread = nioThreadGroup.allocateNioThread(socketChannel);
            nioThread.addChannel(socketChannel);
            // 唤醒NioThread  在没有注册任何事件到selector时，阻塞在select();
            nioThread.selector().wakeup();
            log.info("发生accept事件,client {} 连接,此客户端由线程{}管理", socketChannel.getRemoteAddress(), nioThread.getName());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
