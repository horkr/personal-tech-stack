package com.horkr.jdk.learn.concurrency.aqs;

import com.horkr.util.ThreadUtil;

import java.util.concurrent.TimeUnit;

/**
 * 可重入的原因,如果不是可重入，那么m2是调用不了的
 * @author 卢亮宏
 */
public class Reentrant {

    synchronized void m1(){
        for (int i = 0; i < 10; i++) {
            ThreadUtil.sleep(1, TimeUnit.SECONDS);
            if(i%2==0){
                m2();
            }
        }
    }

    synchronized void m2(){
        System.out.println("m2");
    }


    public static void main(String[] args) {
        new Thread(()->{
            new Reentrant().m1();
        }).start();
    }
}
