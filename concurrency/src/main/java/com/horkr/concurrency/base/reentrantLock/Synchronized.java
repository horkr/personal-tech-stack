package com.horkr.concurrency.base.reentrantLock;

import com.horkr.cloud.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class Synchronized {

    public static void main(String[] args) {
        case2();
    }

    /**
     *  如果是两个线程争取同一把锁，需要第一把锁释放后才可以
     */
    public static void case1(){
        Synchronized instance = new Synchronized();
        ThreadUtil.startByNewThread(instance::reentrant);
        ThreadUtil.sleep(TimeUnit.SECONDS,1);
        ThreadUtil.startByNewThread(instance::print);
    }

    /**
     *  如果是一个线程争取同一把锁，是可重入的，验证了synchronized的可重入性
     */
    public static void case2(){
        Synchronized instance = new Synchronized();
        ThreadUtil.startByNewThread(instance::reentrant1);
    }


    public synchronized void reentrant() {
        for (int i = 0; i < 10; i++) {
            ThreadUtil.sleep(TimeUnit.SECONDS,1);
            System.out.println(i);
        }
    }

    public synchronized void reentrant1() {
        for (int i = 0; i < 10; i++) {
            ThreadUtil.sleep(TimeUnit.SECONDS,1);
            System.out.println(i);
            if(i==2){
                print();
            }
        }
    }

    public synchronized void print() {
        System.out.println("m2");
    }
}
