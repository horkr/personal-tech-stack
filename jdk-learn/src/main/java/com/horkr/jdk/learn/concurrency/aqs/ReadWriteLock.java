package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * m1()互斥锁，所有读操作要顺序执行
 * m2()用读写锁，读操作可以同时进行，效率大大提升
 * @author 卢亮宏
 */
public class ReadWriteLock {

    int val = 0;

    Lock lock = new ReentrantLock();

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    Lock readLock = readWriteLock.readLock();

    Lock writeLock = readWriteLock.writeLock();


    public void read(Lock lock){
        lock.lock();
        System.out.println("read");
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        lock.unlock();
    }

    public void write(Lock lock,int val){
        lock.lock();
        System.out.println("write");
        ThreadUtil.sleep(1,TimeUnit.SECONDS);
        this.val = val;
        lock.unlock();
    }

    public static void m1(){
        ReadWriteLock readWriteLock = new ReadWriteLock();
        for (int i = 0; i < 20; i++) {
            ThreadUtil.startByNewThread(()->readWriteLock.read(readWriteLock.lock));
        }
        for (int i = 0; i < 2; i++) {
            ThreadUtil.startByNewThread(()->readWriteLock.write(readWriteLock.lock,2));
        }
    }

    public static void m2(){
        ReadWriteLock readWriteLock = new ReadWriteLock();
        for (int i = 0; i < 20; i++) {
            ThreadUtil.startByNewThread(()->readWriteLock.read(readWriteLock.readLock));
        }
        for (int i = 0; i < 2; i++) {
            ThreadUtil.startByNewThread(()->readWriteLock.write(readWriteLock.writeLock,2));
        }
    }

    public static void main(String[] args) {
        m2();
    }
}
