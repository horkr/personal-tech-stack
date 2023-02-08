package com.horkr.jdk.learn.network.netty.rpc.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;

/**
 * @author 卢亮宏
 */
public class ClientFactory {
    private static final ClientFactory instance;

    static {
        instance = new ClientFactory();
    }

    public static ClientFactory getInstance() {
        return instance;
    }

    private ClientFactory() {
    }

    public NioSocketChannel getClient(InetAddress address) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChannelInboundHandlerAdapter());
                    }
                });
    }
}
