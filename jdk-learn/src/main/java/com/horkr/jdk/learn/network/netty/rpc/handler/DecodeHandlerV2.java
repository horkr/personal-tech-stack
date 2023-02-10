package com.horkr.jdk.learn.network.netty.rpc.handler;

import com.horkr.jdk.learn.network.netty.rpc.message.Message;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.List;

public class DecodeHandlerV2 extends ByteToMessageDecoder {

    public final Logger log = LoggerFactory.getLogger(DecodeHandlerV2.class);


    /**
     * @param ctx     ctx
     * @param byteBuf 收到的消息
     * @param out     out 反序列化后的对象存储再out中，会通过遍历的方式传递给下一个channelHandler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        int headerLength = 132;
        // 如果剩余可读的字节超过headerLength才继续
        while (byteBuf.readableBytes() >= headerLength) {
            // 132为header的字节数，int+long+long
            byte[] headerBytes = new byte[headerLength];
            // 注意这里，我们不能用readBytes，因为不确定这次循环header和body是否能成对读到，直接read会改变readIndex
            byteBuf.getBytes(byteBuf.readerIndex(), headerBytes);
            // 先读取header
            MessageHeader header = ObjSerializer.bytes2Obj(headerBytes, MessageHeader.class);

            // 如果这次循环header和body能成对读到，先跳到headerLength后的长度读取body
            if (byteBuf.readableBytes() >= headerLength + header.getBodyLength()) {
                byteBuf.readBytes(headerLength);
                byte[] bodyBytes = new byte[(int) header.getBodyLength()];
                byteBuf.readBytes(bodyBytes);
                // 读取body
                MessageBody body = ObjSerializer.bytes2Obj(bodyBytes, MessageBody.class);
                Message message = new Message(header, body);
                out.add(message);
            } else {
                break;
            }
        }
    }
}
