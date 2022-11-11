package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.ThreadUtil;

import java.util.concurrent.Exchanger;

/**
 * 线程间交换数据
 * @author 卢亮宏
 */
public class ExchangerDemo {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        ThreadUtil.startByNewThread(()->{
            Thread.currentThread().setName("T1");
            String x = "t1";
            try {
                x = exchanger.exchange(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+x);
        });

        ThreadUtil.startByNewThread(()->{
            Thread.currentThread().setName("T2");
            String x = "t2";
            try {
                x = exchanger.exchange(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+x);
        });
    }
}
