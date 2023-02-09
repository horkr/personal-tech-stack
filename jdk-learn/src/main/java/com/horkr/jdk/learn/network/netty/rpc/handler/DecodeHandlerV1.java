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

public class DecodeHandlerV1 extends ChannelInboundHandlerAdapter {
    public final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读事件获取到的byteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        // 获取字节数大小
        int fullLength = byteBuf.readableBytes();
        // 132为header的字节数，int+long+long
        byte[] headerBytes = new byte[132];
        byteBuf.readBytes(headerBytes);
        // 1. 先读取header
        MessageHeader header = ObjSerializer.bytes2Obj(headerBytes, MessageHeader.class);

        byte[] bodyBytes = new byte[(int) header.getBodyLength()];
        byteBuf.readBytes(bodyBytes);
        // 2. 读取body
        MessageBody body = ObjSerializer.bytes2Obj(bodyBytes, MessageBody.class);
        log.info("接收到远程:{}调用，原始子节：{},header:{} body:{}", socketAddress, fullLength, header, body);
    }
}
