package com.horkr.jdk.learn.network.netty;

import com.horkr.jdk.learn.network.netty.handler.ClientBoundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author 卢亮宏
 */
public class StartClient1 {
    public static void main(String[] args) throws Exception {
        // eventLoopGroup继承了ExecutorService，也是一个线程池
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);

        NioSocketChannel socketChannel = new NioSocketChannel();

        // 为啥注册不用等,注册到eventLoopGroup，相当于注册到selector
        eventLoopGroup.register(socketChannel);

        //响应式：CustomHandler定义了读取服务端消息，读什么会写什么
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new ClientBoundHandler());

        ChannelFuture connectFuture = socketChannel.connect(new InetSocketAddress("150.158.10.179",9090));
        // 异步的，等待连接完成
        ChannelFuture sync = connectFuture.sync();

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello server".getBytes(StandardCharsets.UTF_8));
        ChannelFuture sendFuture = socketChannel.writeAndFlush(byteBuf);
        // 异步的，等待发送完成
        sendFuture.sync();

        // 因为上边都是异步的，所以要等到客户端关闭。也就时主线程阻塞在这里，以便客户端能一直运行
        sync.channel().closeFuture().sync();
        System.out.println("client over....");
    }
}
