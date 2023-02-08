package com.horkr.jdk.learn.network.netty;


import com.horkr.jdk.learn.network.netty.handler.ClientBoundHandler;
import com.horkr.jdk.learn.network.netty.handler.ServerBoundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author 卢亮宏
 */
public class NettyClient3 {
    public static void main(String[] args) {
        ChannelFuture connectFuture = new Bootstrap().group(new NioEventLoopGroup(1))
                // 客户端用SocketChannel
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientBoundHandler());
                    }
                })
                .connect(new InetSocketAddress("150.158.10.179",9090));

        try {
            // 连接时异步的，等待连接完成
            ChannelFuture sync = connectFuture.sync();
            Channel channel = sync.channel();

            //连接完发送数据
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello server".getBytes(StandardCharsets.UTF_8));
            ChannelFuture sendFuture = channel.writeAndFlush(byteBuf);
            sendFuture.sync();

            // 进行阻塞，一直到客户端关闭
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
