package com.horkr.jdk.learn.network.netty.rpc.handler;

import com.horkr.jdk.learn.network.netty.rpc.core.ResponseCallBackManager;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readableBytes() >= 132) {
            byte[] headerBytes = new byte[132];
            byteBuf.getBytes(byteBuf.readerIndex(), headerBytes);
            MessageHeader header = ObjSerializer.bytes2Obj(headerBytes, MessageHeader.class);
            log.info("客户端接收到调用返回!");
            Runnable callBack = ResponseCallBackManager.getCallBack(header.getRequestId());
            callBack.run();
        }
    }
}
