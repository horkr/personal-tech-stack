package com.horkr.algorithm.verification;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理模式
 * @author 卢亮宏
 */
public class JdkProxyMode {
    interface Face{
        String execute(String param);
    }

    public static void main(String[] args) {
        Face newProxyInstance = (Face) Proxy.newProxyInstance(JdkProxyMode.class.getClassLoader(), new Class[]{Face.class}, new InvocationHandler() {
            @Override
            /**
             * @param proxy 代理的实例，其实就是newProxyInstance自己，一般没什么用
             * @param method 要执行的方法
             * @param args  方法的参数
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
        String zzz = newProxyInstance.execute("zzz");
        System.out.println(zzz);
    }
}
