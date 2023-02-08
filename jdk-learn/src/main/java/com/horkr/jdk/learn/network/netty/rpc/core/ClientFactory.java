package com.horkr.jdk.learn.network.netty.rpc.core;

import cn.hutool.core.util.RandomUtil;
import com.horkr.jdk.learn.network.netty.rpc.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

/**
 * @author 卢亮宏
 */
public class ClientFactory {
    private static final ClientFactory instance;

    private static int poolSize;

    public static void configPoolSize(int size) {
        poolSize = size;
    }

    static {
        instance = new ClientFactory();
    }

    private static ConcurrentHashMap<InetSocketAddress, ClientPool> cache = new ConcurrentHashMap(poolSize);


    public NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = cache.get(address);
        if (isNull(clientPool)) {
            clientPool = new ClientPool(poolSize);
            cache.put(address, clientPool);
        }
        int clientIndex = RandomUtil.getRandom().nextInt(poolSize);
        NioSocketChannel client = clientPool.getClient(clientIndex);
        if (isNull(client)) {
            Object lock = clientPool.getLocks().get(clientIndex);
            synchronized (lock) {
                if (isNull(client)) {
                    client = createClient(address);
                    clientPool.getPool().set(clientIndex, client);
                }
            }
        }
        return client;
    }

    public NioSocketChannel createClient(InetSocketAddress address) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        ChannelFuture future = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientHandler());
                    }
                })
                .connect(address);
        try {
            return (NioSocketChannel) future.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ClientFactory getInstance() {
        return instance;
    }

    private ClientFactory() {
    }
}
