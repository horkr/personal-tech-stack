package com.horkr.jdk.learn.network.netty.rpc.core;

import com.horkr.jdk.learn.network.netty.rpc.handler.DecodeHandlerV2;
import com.horkr.jdk.learn.network.netty.rpc.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer {
    private final Logger log = LoggerFactory.getLogger(RpcServer.class);
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.start();
    }

    /**
     * 创建一个rpc远程调用 server
     */
    public void start(){
        ChannelFuture bind = new ServerBootstrap()
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup(10))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new DecodeHandlerV2());
                        pipeline.addLast(new ServerHandler());
                    }
                })
                .bind(9090);
        try {
            ChannelFuture sync = bind.sync();
            log.info("server start--------------");
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
