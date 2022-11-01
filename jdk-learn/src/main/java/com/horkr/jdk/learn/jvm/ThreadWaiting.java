package com.horkr.jdk.learn.jvm;

/**
 * https://jcjspmsqiu.feishu.cn/wiki/wikcnCwJnxRWh7ISGVfG9eAWgxc
 * @author 卢亮宏
 */
public class ThreadWaiting {
    final static Object obj = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            Thread.currentThread().setName("first");
            synchronized (obj) {
               while (true){

               }
            }
        }).start();


        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                Thread.currentThread().setName("myThread-" + finalI);
                synchronized (obj) {
                    // do something
                }
            }).start();
        }
    }

}
