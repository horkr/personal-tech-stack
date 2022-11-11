package com.horkr.jdk.learn.concurrency.aqs;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static com.horkr.util.ThreadUtil.*;

/**
 * 将List改造为固定容量同步容器，拥有put和get方法，以及getCount方法（不用size()），能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *
 * @author 卢亮宏
 */
public class InterviewQuestionTwo {

    /**
     * 通过wait和singalAll实现
     */
    public static class Container<T> {
        /**
         * 容量
         */
        private final long capacity;

        /**
         * 容器中元素总数
         */
        private long count;


        private final LinkedList<T> list = new LinkedList<>();

        public synchronized void put(T item) {
            // 用while的原因是，如果当前线程wait后被唤醒去put元素，可能另外一个生产者线程已经让容器达到了capacity的值，所以被唤醒执行时要再判断一下。
            // 这里要记住一个原则。但凡有条件的wait都要考虑下条件判断是否得用while
            while (count >= capacity) {
                objWait(this);
            }
            list.add(item);
            count++;
            // 此程序的缺点就是唤醒时，可能也会把另外一个生产者线程唤醒
            objNotifyAll(this);
        }

        public synchronized T get() {
            while (count == 0) {
                objWait(this);
            }
            T t = list.removeFirst();
            count--;
            objNotifyAll(this);
            return t;
        }

        public long getCount() {
            return count;
        }

        public Container(long capacity) {
            this.capacity = capacity;
        }
    }

    /**
     * 通过condition实现。这是最正确的版本
     */
    public static class ContainerWithCondition<T> {
        /**
         * 容量
         */
        private final long capacity;

        /**
         * 容器中元素总数
         */
        private long count;


        private final LinkedList<T> list = new LinkedList<>();

        private ReentrantLock lock = new ReentrantLock();

        private Condition producer = lock.newCondition();
        private Condition consumer = lock.newCondition();

        public void put(T item) {
            try {
                lock.lock();
                while (count >= capacity) {
                    producer.await();
                }
                list.add(item);
                count++;
                consumer.signalAll();
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }

        public T get() {
            try {
                lock.lock();
                while (count == 0) {
                    consumer.await();
                }
                T t = list.removeFirst();
                count--;
                producer.signalAll();
                return t;
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
            return null;
        }

        public long getCount() {
            return count;
        }

        public ContainerWithCondition(long capacity) {
            this.capacity = capacity;
        }
    }


    private static void containerTest() {
//        Container<Integer> container = new Container<>(5);
        ContainerWithCondition<Integer> container = new ContainerWithCondition<>(5);
        startByNewThread(() -> {
            for (int j = 1; j <= 10; j++) {
                container.put(j);
                print("put:%s", j);
            }
        });

        startByNewThread(() -> {
            for (int j = 11; j <= 20; j++) {
                container.put(j);
                print("put:%s", j);
            }
        });

        for (int i = 0; i < 10; i++) {
            startByNewThread(() -> {
                for (int j = 0; j < 10; j++) {
                    Integer item = container.get();
                    print("get:%s", item);
                }
            });
        }
    }

    public static void main(String[] args) {
        containerTest();
    }
}
