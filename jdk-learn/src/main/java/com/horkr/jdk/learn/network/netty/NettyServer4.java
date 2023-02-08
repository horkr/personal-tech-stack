package com.horkr.jdk.learn.network.netty;


import com.horkr.jdk.learn.network.netty.handler.ClientBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author 卢亮宏
 */
public class NettyServer4 {
    public static void main(String[] args) {
        ChannelFuture bindFuture = new ServerBootstrap()
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup(3))
                // 服务端用NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                // accept接收并分发动作netty已经做了，这里只需要处理accpet产生的客户端channel事件即可
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientBoundHandler());
                    }
                })
                .bind(new InetSocketAddress(9099));

        try {
            // 连接时异步的，等待连接完成
            ChannelFuture sync = bindFuture.sync();
            Channel channel = sync.channel();
            // 进行阻塞，一直到服务端关闭
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
