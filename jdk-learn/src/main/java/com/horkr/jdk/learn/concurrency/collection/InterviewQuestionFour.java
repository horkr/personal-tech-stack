package com.horkr.jdk.learn.concurrency.collection;

import com.horkr.util.thread.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

/**
 * 模拟有一万张车票，10个人一起卖，不可超卖
 *
 * @author 卢亮宏
 */
public class InterviewQuestionFour {

    public static void list() {
        List<Integer> tickets = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            tickets.add(i);
        }

        for (int i = 0; i < 10; i++) {
            ThreadUtil.startByNewThread(() -> {
                while (tickets.size() > 0) {
                    // 当size为1时，很多线程都能走到这一步，最终只会有一个卖出票，其他的就会有问题
                    ThreadUtil.print("卖出票：%s", tickets.remove(0));
                }
            });
        }
    }

    public static void vector() {
        Vector<Integer> tickets = new Vector<>(10000);
        for (int i = 0; i < 10000; i++) {
            tickets.add(i);
        }

        for (int i = 0; i < 10; i++) {
            ThreadUtil.startByNewThread(() -> {
                while (tickets.size() > 0) {
                    // vector内部很多方法都是synchronized的，但是同步size()和remove()各自是原子的，放在一块却不是原子的.中间有代码不是原子的
                    // 同样还会有很多线程判断到size=1 进入到循环中
                    ThreadUtil.sleep(1, TimeUnit.MILLISECONDS);
                    ThreadUtil.print("卖出票：%s", tickets.remove(0));
                }
            });
        }
    }

    /**
     * 改良版
     */
    public static void vectorFix() {
        Vector<Integer> tickets = new Vector<>(10000);
        for (int i = 0; i < 10000; i++) {
            tickets.add(i);
        }

        Object lock = new Object();
        for (int i = 0; i < 10; i++) {
            ThreadUtil.startByNewThread(() -> {
                synchronized (lock) {
                    while (tickets.size() > 0) {
                        ThreadUtil.sleep(1, TimeUnit.MILLISECONDS);
                        ThreadUtil.print("卖出票：%s", tickets.remove(0));
                    }
                }
            });
        }
    }

    /**
     * 改良版
     */
    public static void concurrentLinkedQueue() {
        ConcurrentLinkedQueue<Integer> tickets = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 10000; i++) {
            tickets.add(i);
        }

        for (int i = 0; i < 10; i++) {
            ThreadUtil.startByNewThread(() -> {
                while (true) {
                    Integer poll = tickets.poll();
                    if (isNull(poll)) {
                        ThreadUtil.print("卖完了");
                        break;
                    }
                    ThreadUtil.print("卖出票：%s", poll);
                }
            });
        }
    }

    public static void main(String[] args) {
        concurrentLinkedQueue();
    }
}
