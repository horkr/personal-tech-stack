package com.horkr.jdk.learn.network.netty.rpc.core;

import com.horkr.jdk.learn.network.netty.rpc.message.MessageBody;
import com.horkr.jdk.learn.network.netty.rpc.message.MessageHeader;
import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 卢亮宏
 */
public class RpcProxyFactory {

    public static T getProxy(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //1，调用 服务，方法，参数  ==》 封装成message  [content]
                MessageBody messageBody = new MessageBody(clazz.getName(), method.getName(), method.getParameterTypes(), args, null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(messageBody);
                byte[] bodyBytes = byteArrayOutputStream.toByteArray();
                // 清空输出流
                MessageHeader messageHeader = new MessageHeader(RandomUtils.nextInt(), RandomUtils.nextLong(), bodyBytes.length);
                byteArrayOutputStream.reset();
                objectOutputStream.writeObject(messageHeader);
                byte[] headerBytes = byteArrayOutputStream.toByteArray();
                //2，requestID+message  ，本地要缓存


                //3，连接池：：取得连接


                //4，发送--> 走IO  out -->走Netty（event 驱动）


                //5，？，如果从IO ，未来回来了，怎么将代码执行到这里
                return null;
            }
        });
    }
}
