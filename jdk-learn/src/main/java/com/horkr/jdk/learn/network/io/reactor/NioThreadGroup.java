package com.horkr.jdk.learn.network.io.reactor;

import com.horkr.util.load_balance.LoopLoadBalancer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * nio的线程组，包含了处理时间的线程集合，
 * @author 卢亮宏
 */
public class NioThreadGroup {
    private static final Logger log = LogManager.getLogger(NioThreadGroup.class);
    /**
     * 线程组持有的所有NIO线程
     */
    private final List<NioThread> threads;

    /**
     * 处理非accept时间的线程组
     */
    private NioThreadGroup workerGroup;

    /**
     * 线程组的名字
     */
    private String name;

    /**
     * 轮询的负载均衡器，负责分配accept到的客户端channel到不同的NIO线程中处理
     */
    private final LoopLoadBalancer<NioThread> loopLoadBalancer;

    public NioThreadGroup(int threadNum, String name) {
        this.threads = new ArrayList<>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            NioThread nioThread = new NioThread(this);
            nioThread.setName(name + "-" + nioThread.getName());
            threads.add(nioThread);
            // 启动线程
            nioThread.start();
        }
        this.loopLoadBalancer = new LoopLoadBalancer<>(threads);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkerGroup(NioThreadGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public NioThreadGroup getWorkerGroup() {
        return workerGroup;
    }

    /**
     * 通过负载均衡器，分配一个NioThread.如果是创建服务获取的ServerSocketChannel分配给bossGroup（负责处理accept事件）中的线程
     * 否则分配给负责处理非accept事件的workerGroup中的线程处理
     *
     * @return NioThread
     */
    public NioThread allocateNioThread(Channel channel) {
        if (channel instanceof ServerSocketChannel) {
            return loopLoadBalancer.obtainResource();
        } else if (channel instanceof SocketChannel) {
            return workerGroup.loopLoadBalancer.obtainResource();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 创建一个Server
     * 1. 创建一个Server
     * 2. 通过负载均衡分配一个持有selector的NioThread
     * 3. 将serverChannel交给分配的NioThread处理（register OP_ACCEPT）
     *
     * @param port port
     */
    public void createServer(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            NioThread nioThread = allocateNioThread(serverSocketChannel);
            log.info("server由线程接管：{}", nioThread.getName());
            nioThread.addChannel(serverSocketChannel);
            nioThread.selector().wakeup();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
