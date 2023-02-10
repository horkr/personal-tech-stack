package com.horkr.jdk.learn.network.netty.rpc.core;

import com.horkr.jdk.learn.network.netty.rpc.service.CustomService;
import com.horkr.util.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainTest {
    public static final Logger log = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args) {
        ClientFactory.configPoolSize(1);
        CustomService customService = RpcProxyFactory.getProxy(CustomService.class);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            ThreadUtil.startByNewThread("client-bus-thread-"+i,()->{
                String update = customService.update(finalI);
                log.info("远程调用执行结束----请求参数：{}，调用结果：{}",finalI,update);
            });
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
