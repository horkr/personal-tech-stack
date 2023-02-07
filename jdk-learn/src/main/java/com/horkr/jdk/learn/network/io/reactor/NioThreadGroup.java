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
 * @author 卢亮宏
 */
public class NioThreadGroup {
    private static final Logger log = LogManager.getLogger(NioThreadGroup.class);
    /**
     * 线程组持有的所有NIO线程
     */
    private final List<NioThread> threads;

    private NioThreadGroup workerGroup;

    private String name;

    /**
     * 轮询的负载均衡器
     */
    private final LoopLoadBalancer<NioThread> loopLoadBalancer;

    public NioThreadGroup(int threadNum, String name) {
        this.threads = new ArrayList<>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            NioThread nioThread = new NioThread(this);
            nioThread.setName(name + "-" + nioThread.getName());
            threads.add(nioThread);
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
     * 分配一个NioThread
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
     * 4.
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
