package com.horkr.jdk.learn.network.netty.handler;

import com.horkr.jdk.learn.network.io.reactor.NioThread;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 卢亮宏
 */
public class ClientBoundHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(ClientBoundHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("client  registed...");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client active...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
//        CharSequence str = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
        CharSequence str = buf.getCharSequence(0,buf.readableBytes(), CharsetUtil.UTF_8);
        log.info(str.toString());
        ctx.writeAndFlush(buf);
    }
}
