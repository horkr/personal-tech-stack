package com.horkr.jdk.learn.concurrency.thread_pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 卢亮宏
 */
public class Demo {
    public static final Logger log = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 0, TimeUnit.SECONDS, new SynchronousQueue<>());
        Future<Integer> submit = threadPoolExecutor.submit(() -> {
            Thread.sleep(10000);
            return 3;
        });
        try {
            submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
