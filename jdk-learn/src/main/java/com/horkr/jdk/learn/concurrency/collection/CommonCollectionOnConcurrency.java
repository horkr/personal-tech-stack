package com.horkr.jdk.learn.concurrency.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger log = LogManager.getLogger(CommonCollectionOnConcurrency.class);

    /**
     * 优先队列，总是排序最小的先出来
     */
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


    private static void delayQueue() throws InterruptedException {
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        queue.offer(new DelayedTask(4,timeUnit));
        queue.offer(new DelayedTask(2,timeUnit));
        queue.offer(new DelayedTask(5,timeUnit));
        queue.offer(new DelayedTask(3,timeUnit));
        queue.offer(new DelayedTask(1,timeUnit));
        while (queue.size() != 0) {
            log.info(queue.take().getRunningTime());
        }
    }

    public static class DelayedTask implements Delayed {

        /**
         * 在队列中延迟delayTime后执行
         *
         * @param delayTime 延迟时间
         */
        public DelayedTask(long delayTime, TimeUnit timeUnit) {
            this.runningTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delayTime, timeUnit);
        }

        /**
         * 任务被获取到的时间
         */
        private final long runningTime;


        @Override
        public long getDelay(TimeUnit unit) {
            return runningTime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed delay) {
            Comparator<DelayedTask> comparingLong = Comparator.comparingLong(DelayedTask::getRunningTime);
            return comparingLong.compare(this, (DelayedTask) delay);
        }

        public long getRunningTime() {
            return runningTime;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        delayQueue();
    }
}
