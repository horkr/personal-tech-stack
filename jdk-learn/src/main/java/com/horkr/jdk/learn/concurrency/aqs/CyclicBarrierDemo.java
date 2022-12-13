package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.thread.ThreadUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 等到数据达到一定数量时在执行后续逻辑
 *
 * @author 卢亮宏
 */
public class CyclicBarrierDemo {

    public void m1() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(20, () -> {
            System.out.println("冲");
        });
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            ThreadUtil.startByNewThread(() -> {
                try {
                    cyclicBarrier.await();
                    System.out.println(finalI);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new CyclicBarrierDemo().m1();
    }
}
