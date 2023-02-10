package com.horkr.jdk.learn.network.netty.rpc.core;

import cn.hutool.core.util.RandomUtil;
import com.horkr.jdk.learn.network.netty.rpc.handler.ClientHandler;
import com.horkr.jdk.learn.network.netty.rpc.handler.DecodeHandlerV2;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

/**
 * 客户端连接生产工厂，持有着固定数量的连接
 *
 * @author 卢亮宏
 */
public class ClientFactory {
    public final Logger log = LoggerFactory.getLogger(ClientFactory.class);
    private static final ClientFactory instance;
    /**
     * 客户端连接池的大小，这里的连接池是以服务端address隔离的，即每个address有poolSize个链接
     */
    private static int poolSize;

    /**
     * 配置 客户端连接池的大小
     *
     * @param size size
     */
    public static void configPoolSize(int size) {
        poolSize = size;
    }

    static {
        instance = new ClientFactory();
    }

    /**
     * 已存在的客户端连接缓存
     */
    private static ConcurrentHashMap<InetSocketAddress, ClientPool> cache = new ConcurrentHashMap<>(poolSize);


    /**
     * 获取客户端连接,注意这里要是synchronized的
     *
     * @param address 连接的server地址
     * @return NioSocketChannel
     */
    public synchronized NioSocketChannel getClient(InetSocketAddress address) {
        // 1. 先尝试从缓存中获取连接池
        ClientPool clientPool = cache.get(address);
        if (isNull(clientPool)) {
            clientPool = new ClientPool(poolSize);
            cache.put(address, clientPool);
        }
        // 2. 从连接池中随机取一个
        int clientIndex = RandomUtil.getRandom().nextInt(poolSize);
        NioSocketChannel client = clientPool.getClient(clientIndex);
        // 3. 连接池中不存在，则新创建一个client
        if (isNull(client)) {
            client = createClient(address);
            log.info("创建了新的客户端：{}", client.localAddress());
            clientPool.getPool().set(clientIndex, client);
        }
        return client;
    }

    /**
     * 创建客户端client
     *
     * @param address 服务端地址
     * @return NioSocketChannel
     */
    public NioSocketChannel createClient(InetSocketAddress address) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        ChannelFuture future = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加反序列化handler
                        pipeline.addLast(new DecodeHandlerV2());
                        // 处理server发送的数据
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
