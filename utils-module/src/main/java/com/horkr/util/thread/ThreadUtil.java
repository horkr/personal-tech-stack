package com.horkr.util.thread;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ThreadUtil {
    public static void sleep(TimeUnit timeUnit, long time) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ignore) {
            // ignore
        }
    }

    public static void sleep(long time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ignore) {
            // ignore
        }
    }

    public static Thread startByNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public static void systemInRead() {
        try {
            System.in.read();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static Thread startByNewThread(String threadName, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadName);
        thread.start();
        return thread;
    }

    public static void objWait(Object lock,long time) {
        try {
            lock.wait(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void objWait(Object lock) {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException ignore) {
        }
    }


    public static void objNotify(Object lock) {
        lock.notify();
    }

    public static void objNotifyAll(Object lock) {
        lock.notifyAll();
    }

    public static void print(String msg, Object... params) {
        System.out.println(Thread.currentThread().getName() + ":" + String.format(msg, params));
    }



}
