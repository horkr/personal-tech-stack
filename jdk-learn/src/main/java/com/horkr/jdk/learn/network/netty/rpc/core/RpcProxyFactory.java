package com.horkr.jdk.learn.network.netty.rpc.core;

import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import com.horkr.jdk.learn.network.netty.rpc.serializ.ObjSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 接口RPC调用代理生成工厂
 *
 * @author 卢亮宏
 */
public class RpcProxyFactory {
    public final Logger log = LoggerFactory.getLogger(RpcProxyFactory.class);

    /**
     * 获取接口的RPC代理实现
     *
     * @param clazz 接口类
     * @return T
     */
    public static <T> T getProxy(Class<T> clazz) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            Logger log = LoggerFactory.getLogger("RPC-PROXY");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //1，调用服务的方法，参数封装成MessageBody
                MessageBody messageBody = new MessageBody(clazz.getName(), method.getName(), method.getParameterTypes(), args, null);
                byte[] bodyBytes = ObjSerializer.obj2Bytes(messageBody);

                //2，请求的唯一标识，状态标记，请求体大小封装成MessageHeader
                MessageHeader messageHeader = new MessageHeader(100, Math.abs(UUID.randomUUID().getLeastSignificantBits()), bodyBytes.length);
                byte[] headerBytes = ObjSerializer.obj2Bytes(messageHeader);

                //3，获取客户端连接
                ClientFactory clientFactory = ClientFactory.getInstance();
                NioSocketChannel client = clientFactory.getClient(new InetSocketAddress(9090));
                //4，序列化消息并发送给客户端
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + bodyBytes.length);
                byteBuf.writeBytes(headerBytes);
                byteBuf.writeBytes(bodyBytes);
                log.info("远程调用已发起，等待返回！RequestId:{},Interface:{} Method:{}", messageHeader.getRequestId(), clazz.getName(), method.getName());
                /**
                 * 5.这里发送了消息，但是invoke是接口真正执行的方式，是要返回结果的，
                 * 所以这里定义了 CompletableFuture，在发送完消息后等待，服务端返回执行结果
                 * 服务端返回是触发客户端channel的read事件，但是read事件是NioEventLoopGroup中的线程处理 {@link com.horkr.jdk.learn.network.netty.rpc.handler.ClientHandler#channelRead(ChannelHandlerContext, Object)}
                 * 所以在read事件线程中我们会对这个CompletableFuture进行complete并返回结果
                 * 另外，这里也可以定义一个{@link java.util.concurrent.CountDownLatch},进行await，然后在read事件线程countDown，也可做到上述效果，但是countDownLatch没有返回值
                 */
                CompletableFuture<String> future = new CompletableFuture<>();
                ResponseCallBackManagerV2.registerCallBack(messageHeader.getRequestId(), future);
                ChannelFuture writeFuture = client.writeAndFlush(byteBuf);
                writeFuture.sync();
                //6，等待服务端返回的执行结果。
                return future.get();
            }
        };
        // 返回代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }
}
