package com.horkr.jdk.learn.concurrency.aqs;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 卢亮宏
 */
public class ReentrantLockDemo {

    private ReentrantLock lock = new ReentrantLock();

    private int num = 1;


    public void modifyNum(int targetNum) {
        lock.lock();
        num = targetNum;
        lock.unlock();
    }
}
