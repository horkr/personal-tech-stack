package com.horkr.jdk.learn.jvm;

/**
 * @author 卢亮宏
 */
public class HighCpu {
    private static volatile boolean flag = true;


    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            while (flag) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("cpu线程结束。。。");
        }, "cpu-thread");
        thread.start();
        System.in.read();
        flag = false;
    }
}
