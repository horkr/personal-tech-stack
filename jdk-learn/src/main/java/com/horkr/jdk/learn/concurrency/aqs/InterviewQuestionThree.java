package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 3. 写两个线程，一个线程打印1-52，另一个线程打印A-Z，打印结果为12A34B...5152Z
 *
 * @author 卢亮宏
 */
public class InterviewQuestionThree {

    public static void sync() {
        Object lock = new Object();
        ThreadUtil.startByNewThread("t1", () -> {
            synchronized (lock) {
                for (int i = 1; i <= 52; i++) {
                    ThreadUtil.print(String.valueOf(i));
                    if (i % 2 == 0) {
                        lock.notifyAll();
                        ThreadUtil.objWait(lock);
                    }
                }
            }
        });

        ThreadUtil.sleep(1, TimeUnit.SECONDS);

        ThreadUtil.startByNewThread("t1", () -> {
            char A = 'A';
            synchronized (lock) {
                for (int i = 1; i <= 26; i++) {
                    ThreadUtil.print(String.valueOf(A));
                    A++;
                    lock.notifyAll();
                    ThreadUtil.objWait(lock);
                }
            }
        });
    }

    public static void lock() {
        ReentrantLock lock = new ReentrantLock();
        Condition numCondition = lock.newCondition();
        Condition charCondition = lock.newCondition();
        ThreadUtil.startByNewThread("t1", () -> {
            lock.lock();
            for (int i = 1; i <= 52; i++) {
                ThreadUtil.print(String.valueOf(i));
                if (i % 2 == 0) {
                    charCondition.signal();
                    try {
                        numCondition.await();
                    } catch (InterruptedException e) {
                    }
                }
            }
            lock.unlock();
        });

        ThreadUtil.sleep(1, TimeUnit.SECONDS);

        ThreadUtil.startByNewThread("t1", () -> {
            lock.lock();
            char A = 'A';
            for (int i = 1; i <= 26; i++) {
                ThreadUtil.print(String.valueOf(A));
                A++;
                numCondition.signal();
                try {
                    charCondition.await();
                } catch (InterruptedException e) {
                }
            }
            lock.unlock();
        });
    }

    public static void main(String[] args) {
        lock();
    }
}
