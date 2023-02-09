package com.horkr.jdk.learn.network.netty.rpc.handler;

import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    public final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        ByteBuf sendByteBuf = byteBuf.copy();
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        int fullLength = byteBuf.readableBytes();
        if (byteBuf.readableBytes() >= 132) {
            byte[] headerBytes = new byte[132];
            byteBuf.readBytes(headerBytes);
            MessageHeader header = ObjSerializer.bytes2Obj(headerBytes, MessageHeader.class);

            if (byteBuf.readableBytes() >= header.getBodyLength()) {
                byte[] bodyBytes = new byte[(int) header.getBodyLength()];
                byteBuf.readBytes(bodyBytes);

                MessageBody body = ObjSerializer.bytes2Obj(bodyBytes, MessageBody.class);
                log.info("接收到远程:{}调用，原始子节：{},header:{} body:{}",socketAddress,fullLength, header, body);
            } else {
                log.error("服务端读取body出错，原始子节：{}，剩余子节：{}", fullLength, byteBuf.readableBytes());
            }
            ctx.writeAndFlush(sendByteBuf);
        }

    }
}
