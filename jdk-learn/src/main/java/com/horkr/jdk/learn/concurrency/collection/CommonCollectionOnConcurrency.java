package com.horkr.jdk.learn.concurrency.collection;

import com.horkr.util.thread.ThreadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * 并发常用集合
 * https://jcjspmsqiu.feishu.cn/wiki/wikcnGVhbL13WbtT9y9FVJbXVUd#
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

    /**
     * 基于优先队列 PriorityQueue 实现，每个任务按照一定延迟后才会被获取到
     * @throws InterruptedException
     */
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

    /**
     * 用于两个线程交换数据，类似于Exchanger的功能,可以理解成一个线程手把手把东西交给另一个线程，如果没有另一个线程则拿在手里等着。如果没有另一个线程在等待take()，那么put()也会一直等着。类似于Exchanger的功能。主要用于线程池
     * @throws InterruptedException
     */
    private static void synchronousQueue() throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        queue.put(1);
    }


    private static void transferQueue() throws InterruptedException {
        LinkedTransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        ThreadUtil.startByNewThread("t1",()->{
            try {
                Integer take = transferQueue.take();
                log.info("取到：{}",take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ThreadUtil.startByNewThread("t1",()->{
            try {
                transferQueue.transfer(8943);
                log.info("投放的元素已经被取走");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        transferQueue();
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


}
