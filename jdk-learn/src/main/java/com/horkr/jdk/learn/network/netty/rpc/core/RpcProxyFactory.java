package com.horkr.jdk.learn.network.netty.rpc.core;

import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author 卢亮宏
 */
public class RpcProxyFactory {
    public final Logger log = LoggerFactory.getLogger(RpcProxyFactory.class);

    public static <T> T getProxy(Class<T> clazz) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            Logger log = LoggerFactory.getLogger("RPC-PROXY");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //1，调用 服务，方法，参数  ==》 封装成message  [content]
                MessageBody messageBody = new MessageBody(clazz.getName(), method.getName(), method.getParameterTypes(), args, null);
                byte[] bodyBytes = ObjSerializer.obj2Bytes(messageBody);
                // 清空输出流
//                MessageHeader messageHeader = new MessageHeader(RandomUtils.nextInt(), RandomUtils.nextLong(), bodyBytes.length);
                MessageHeader messageHeader = new MessageHeader(100, Math.abs(UUID.randomUUID().getLeastSignificantBits()), bodyBytes.length);
                byte[] headerBytes = ObjSerializer.obj2Bytes(messageHeader);
                //2，requestID+message  ，本地要缓存


                //3，连接池：：取得连接
                ClientFactory clientFactory = ClientFactory.getInstance();
                NioSocketChannel client = clientFactory.getClient(new InetSocketAddress(9090));
                //4，发送--> 走IO  out -->走Netty（event 驱动）
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + bodyBytes.length);
                byteBuf.writeBytes(headerBytes);
                byteBuf.writeBytes(bodyBytes);
                log.info("远程调用已发起，等待返回！RequestId:{},Interface:{} Method:{}", messageHeader.getRequestId(), clazz.getName(), method.getName());
//                CountDownLatch countDownLatch = new CountDownLatch(1);
//                ResponseCallBackManagerV1.registerCallBack(messageHeader.getRequestId(), () -> {
//                    countDownLatch.countDown();
//                    log.info("执行服务端响应回调函数-----");
//                });

                CompletableFuture<String> future = new CompletableFuture<>();
                ResponseCallBackManagerV2.registerCallBack(messageHeader.getRequestId(), future);
                ChannelFuture writeFuture = client.writeAndFlush(byteBuf);
                writeFuture.sync();
//                countDownLatch.await();
                //5，这里要返回从服务端返回的执行结果。如果从IO ，未来回来了，怎么将代码执行到这里
                return future.get();
            }
        };
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }
}
