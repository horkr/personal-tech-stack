package com.horkr.jdk.learn.concurrency.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static com.horkr.util.thread.ThreadUtil.*;

/**
 * 线程状态
 *
 * @author 卢亮宏
 */
public class ThreadState {


    private static void waitState() {
        Object lock = new Object();
        Thread t1 = startByNewThread("t1", () -> {
            synchronized (lock) {
                print("即将Wait...");
                print(Thread.currentThread().getState().name());
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    print("interrupt!");
                }
                print("醒来...");
            }
        });
        t1.interrupt();
    }


    private static void waitState1() {
        Object lock = new Object();
        Thread t1 = startByNewThread("t1", () -> {
            print("即将Wait...");
            print(Thread.currentThread().getState().name());
            LockSupport.park();
            print("醒来...");
        });
        sleep(1, TimeUnit.SECONDS);

        Thread t2 = startByNewThread("t2", () -> {
            synchronized (lock) {
                print("aaa" + t1.getState().name());
                t1.interrupt();
                LockSupport.unpark(t1);
                print("bbb" + t1.getState().name());
            }
        });
    }

    private static void joinState() {
        Thread t1 = startByNewThread("t1", () -> {
            while (true) {

            }
        });
        Thread t2 = startByNewThread("t2", () -> {
            try {
                t1.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        print(t1.getState().name());
        print(t2.getState().name());
    }


    public static void main(String[] args) {
        waitState();
    }

}
