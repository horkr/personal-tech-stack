package com.horkr.jdk.learn.network.io.reactor;

import com.horkr.util.load_balance.LoopLoadBalancer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
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

    /**
     * 轮询的负载均衡器
     */
    private final LoopLoadBalancer<NioThread> loopLoadBalancer;

    public NioThreadGroup(int threadNum) {
        this.threads = new ArrayList<>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            NioThread nioThread = new NioThread(this);
            threads.add(nioThread);
            nioThread.start();
        }
        this.loopLoadBalancer = new LoopLoadBalancer<>(threads);
    }

    /**
     * 分配一个NioThread
     *
     * @return NioThread
     */
    public NioThread allocateNioThread() {
        return loopLoadBalancer.obtainResource();
    }

    /**
     * 创建一个Server
     * 1. 创建一个Server
     * 2. 通过负载均衡分配一个持有selector的NioThread
     * 3. 将serverChannel交给分配的NioThread处理（register OP_ACCEPT）
     * 4. 唤醒NioThread  在没有注册任何事件到selector时，阻塞在select();
     * @param port port
     */
    public void createServer(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            NioThread nioThread = loopLoadBalancer.obtainResource();
            nioThread.addChannel(serverSocketChannel);
            nioThread.selector().wakeup();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
