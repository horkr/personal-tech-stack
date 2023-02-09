package com.horkr.jdk.learn.network.netty.rpc.handler;

import com.horkr.jdk.learn.network.netty.rpc.message.Message;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    public final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String ioThreadName = Thread.currentThread().getName();
        // 获取的是当前线程
        EventExecutor executor = ctx.executor();
        // 从nioEventGroup中取一个
        EventExecutor next = ctx.executor().parent().next();
        next.execute(() -> {
            Message message = (Message) msg;
            MessageHeader header = message.getHeader();
            MessageBody body = message.getBody();
            // TODO 这里通过class method等查找实现类兵执行的逻辑没有实现，以后有时间再写！
            body.setResponse("server handled : " + body.getArgs()[0]);
            byte[] bodyBytes = ObjSerializer.obj2Bytes(body);
            header.setBodyLength(bodyBytes.length);
            header.setFlag(200);
            byte[] headerBytes = ObjSerializer.obj2Bytes(header);
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + bodyBytes.length);
            byteBuf.writeBytes(headerBytes);
            byteBuf.writeBytes(bodyBytes);
            log.info("接收到远程调用，已结束处理！ioThreadName:{}", ioThreadName);
            ctx.writeAndFlush(byteBuf);
        });
    }
}
