package com.horkr.jdk.learn.network.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;

/**
 * @author 卢亮宏
 */
public class ServerBoundHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(ServerBoundHandler.class);
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("server  registed...");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client active...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 因为是server，所以接收到的应该是SocketChannel,相当于accept获取
        SocketChannel client = (SocketChannel) msg;
        /* 在nio中我们accept到client，应该有以下步骤
         1. 注册到selector
         2.
         */
    }
}
