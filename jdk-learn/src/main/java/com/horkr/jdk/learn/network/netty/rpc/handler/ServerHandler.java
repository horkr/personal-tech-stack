package com.horkr.jdk.learn.network.netty.rpc.handler;

import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    public final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readableBytes() >= 132) {
            byte[] headerBytes = new byte[132];
            byteBuf.getBytes(byteBuf.readerIndex(), headerBytes);
            MessageHeader header = ObjSerializer.bytes2Obj(headerBytes, MessageHeader.class);

            byteBuf.readBytes(132);
            byte[] bodyBytes = new byte[(int) header.getBodyLength()];
            byteBuf.readBytes(bodyBytes);
            MessageBody body = ObjSerializer.bytes2Obj(bodyBytes, MessageBody.class);
            log.info("接收到远程调用，header:{} body:{}", header, body);


            ByteBuf sendByteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + bodyBytes.length);
            sendByteBuf.writeBytes(headerBytes);
            sendByteBuf.writeBytes(bodyBytes);
            ctx.writeAndFlush(sendByteBuf);
        }

    }
}
