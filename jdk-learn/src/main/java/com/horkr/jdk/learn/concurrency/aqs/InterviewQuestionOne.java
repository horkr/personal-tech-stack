package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static com.horkr.util.ThreadUtil.*;

/**
 * 开始阅读aqs源码
 *
 * @author 卢亮宏
 */
public class InterviewQuestionOne {

    /**
     * 实现一个容器，提供两个方法，add，size
     * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束线程2
     */


    public static void withoutVolatile() {
        List<Integer> list = new ArrayList<>();
        startByNewThread("T1", () -> {
            for (int i = 0; i < 10; i++) {
                ThreadUtil.sleep(1, TimeUnit.SECONDS);
                list.add(i);
                ThreadUtil.print("add:%s", i);
            }
        });
        startByNewThread("T2", () -> {
            while (true) {
                if (list.size() == 5) {
                    ThreadUtil.print("end");
                    break;
                }
            }
        });
    }


    /**
     * 这里用 volatile 修饰了就有用，但是很奇怪，volatile修饰的是引用，如果指向对象的成员变量变化了，是观察不到的，当然如果去掉sleep还是没有作用。可能是sleep期间虚拟机去读了主存数据
     * 所以，如果没有把握不要随便用volatile
     */
    volatile List<Integer> innerList = new ArrayList<>();

    public void withVolatile() {

        startByNewThread("T1", () -> {
            for (int i = 0; i < 10; i++) {
                ThreadUtil.sleep(1, TimeUnit.SECONDS);
                innerList.add(i);
                ThreadUtil.print("add:%s", i);
            }
        });
        startByNewThread("T2", () -> {
            while (true) {
                if (innerList.size() == 5) {
                    break;
                }
            }
            ThreadUtil.print("end");
        });
    }

    List<Integer> syncList = new ArrayList<>();
    Object lock = new Object();


    /**
     * 通过 synchronized实现，这里还是不行，因为t1虽然唤醒了t2，但是t1依然占用着锁，t2此时只是在等着锁
     */
    public void sync1() {
        startByNewThread("T2", () -> {
            ThreadUtil.print("start");
            synchronized (lock) {
                if (syncList.size() != 5) {
                    ThreadUtil.objWait(lock);
                }
                ThreadUtil.print("end");
            }
        });

        // 保证t2先执行
        sleep(1, TimeUnit.SECONDS);

        startByNewThread("T1", () -> {
            ThreadUtil.print("start");
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    syncList.add(i);
                    print("add:%s", i);

                    if (syncList.size() == 5) {
                        objNotify(lock);
                    }

                    sleep(1, TimeUnit.SECONDS);
                }
            }
        });
    }


    /**
     * 解决sync1的问题
     */
    public void sync2() {
        startByNewThread("T2", () -> {
            ThreadUtil.print("start");
            synchronized (lock) {
                if (syncList.size() != 5) {
                    ThreadUtil.objWait(lock);
                }
                ThreadUtil.print("end");
                // 唤醒t1,让其继续执行
                objNotify(lock);
            }
        });

        // 保证t2先执行
        sleep(1, TimeUnit.SECONDS);

        startByNewThread("T1", () -> {
            ThreadUtil.print("start");
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    syncList.add(i);
                    print("add:%s", i);

                    if (syncList.size() == 5) {
                        // 唤醒t2
                        objNotify(lock);

                        // wait自己，让出执行权给t2
                        objWait(lock);
                    }

                }
            }
        });
    }


    public void withCountDownLatch() {
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        startByNewThread("T2", () -> {
            ThreadUtil.print("start");
            if (syncList.size() != 5) {
                try {
                    latch1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ThreadUtil.print("end");
        });

        // 保证t2先执行
        sleep(1, TimeUnit.SECONDS);

        startByNewThread("T1", () -> {
            ThreadUtil.print("start");
            for (int i = 0; i < 10; i++) {
                syncList.add(i);
                print("add:%s", i);

                if (syncList.size() == 5) {
                    latch1.countDown();
                }

//                sleep(1, TimeUnit.SECONDS);
            }
        });
    }

    static Thread t1 = null, t2 = null;

    public void withLockSupport() {
        t2 = new Thread(() -> {
            print("start");
            LockSupport.park();
            print("end");
            LockSupport.unpark(t1);
        }, "T2");
        t1 = new Thread(() -> {
            print("start");
            for (int i = 0; i < 10; i++) {
                syncList.add(i);
                print("add:%s", i);

                if (syncList.size() == 5) {
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        }, "T1");

        t2.start();
        // 保证t2先执行
        sleep(1, TimeUnit.SECONDS);
        t1.start();

    }


    public static void main(String[] args) {
        new InterviewQuestionOne().withLockSupport();
    }
}
