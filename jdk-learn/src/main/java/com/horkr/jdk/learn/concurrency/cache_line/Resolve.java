package com.horkr.jdk.learn.concurrency.cache_line;

import com.horkr.util.ThreadUtil;

/**
 * @author 卢亮宏
 */
public class Resolve {
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
