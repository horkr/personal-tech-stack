package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.thread.ThreadUtil;
import org.apache.commons.lang3.RandomUtils;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore用于控制并发执行线程数
 *
 * 有很多的饭，每次只允许三个人一起吃
 *
 * @author 卢亮宏
 */
public class SemaphoreDemo {

    private Semaphore semaphore;

    private LinkedList<Integer> FOODS = new LinkedList<>();

    private int eatPersonAtSameTime;


    public SemaphoreDemo(int eatPersonAtSameTime) {
        this.eatPersonAtSameTime = eatPersonAtSameTime;
        semaphore = new Semaphore(eatPersonAtSameTime);
    }

    public void startProduce() {
        ThreadUtil.startByNewThread("producer", () -> {
            for (; ; ) {
                FOODS.add(RandomUtils.nextInt());
            }
        });
    }


    public static void main(String[] args) {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo(3);
        semaphoreDemo.startProduce();
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        for (int i = 0; i < 20; i++) {
            semaphoreDemo.startEat(i);
        }
    }

    public void startEat(int personNo) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
        }

        ThreadUtil.startByNewThread("person-" + personNo, () -> {
            Integer integer = FOODS.removeFirst();
            System.out.println("吃了-" + integer);
            ThreadUtil.sleep(1, TimeUnit.SECONDS);
            semaphore.release();
        });
    }

}
