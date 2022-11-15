package com.horkr.jdk.learn.concurrency.collection;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 并发常用集合
 *
 * @author 卢亮宏
 */
public class CommonCollectionOnConcurrency {

    private static void priorityQueue() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.offer(4);
        queue.offer(2);
        queue.offer(5);
        queue.offer(3);
        queue.offer(1);
        while (queue.size() != 0) {
            System.out.println(queue.poll());
        }
    }


    private static void delayQueue() {
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        queue.offer(new DelayedTask(4));
        queue.offer(new DelayedTask(2));
        queue.offer(new DelayedTask(5));
        queue.offer(new DelayedTask(3));
        queue.offer(new DelayedTask(1));
        while (queue.size() != 0) {
            System.out.println(queue.poll().getRunningTime());
        }
    }

    public static class DelayedTask implements Delayed {

        public DelayedTask(long runningTime) {
            this.runningTime = runningTime;
        }

        /**
         * 执行时间
         */
        private final long runningTime;

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runningTime - System.currentTimeMillis(), unit);
        }

        @Override
        public int compareTo(Delayed delay) {
            Comparator<Delayed> comparingLong = Comparator.comparingLong(d -> d.getDelay(TimeUnit.MILLISECONDS));
            return comparingLong.compare(this, delay);
        }

        public long getRunningTime() {
            return runningTime;
        }
    }

    public static void main(String[] args) {
        delayQueue();
    }
}
