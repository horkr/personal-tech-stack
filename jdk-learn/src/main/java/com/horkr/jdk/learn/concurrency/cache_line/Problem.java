package com.horkr.jdk.learn.concurrency.cache_line;

import com.horkr.util.thread.ThreadUtil;

/**
 * 如下代码，定义了2位的数组，开启两个线程分别修改其中一个数据。理论上是两个cpu处理两个资源，不会产生一致性问题。
 * 但是数组成员为8个子节的long型，一个缓存行为64个子节。那么数组的两个元素可能会被分到一个缓存行。这样就产生了伪共享的问题
 * @author 卢亮宏
 */
public class Problem {
//    static class Resource {
//        private volatile long x = 0l;
//    }
    static class Resource {
        private volatile long p1,p2,p3,p4,p5,p6,p7;
        private volatile long x = 0l;
    }

    public static Resource[] resources = new Resource[2];

    static {
        resources[0] = new Resource();
        resources[1] = new Resource();
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                resources[0].x = i;
            }
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                resources[1].x = i;
            }
        });

        t1.start();
        t2.start();
        ThreadUtil.join(t1);
        ThreadUtil.join(t2);
        System.out.println("custom time:"+(System.currentTimeMillis()-start)+"ms");

    }
}
