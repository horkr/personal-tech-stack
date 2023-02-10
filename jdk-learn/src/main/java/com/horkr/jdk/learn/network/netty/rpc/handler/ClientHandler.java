package com.horkr.jdk.learn.network.netty.rpc.handler;

import com.horkr.jdk.learn.network.netty.rpc.core.ResponseCallBackManagerV2;
import com.horkr.jdk.learn.network.netty.rpc.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端channel处理器
 * 这里收到了服务端返回的数据，带有response，我们要将发送时的CompletableFuture唤醒并返回服务端响应
 * @see com.horkr.jdk.learn.network.netty.rpc.core.RpcProxyFactory#getProxy(Class)
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    public final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        ResponseCallBackManagerV2.runCallBack(message.getHeader().getRequestId(), message.getBody().getResponse());
    }
}
