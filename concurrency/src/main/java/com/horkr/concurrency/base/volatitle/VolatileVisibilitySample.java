package com.horkr.concurrency.base.volatitle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * volatile保证可见性 https://jcjspmsqiu.feishu.cn/wiki/wikcnuJxJA77yJVOtq3MVO8Vqgg
 */
public class VolatileVisibilitySample {
    private final Logger log = LoggerFactory.getLogger(VolatileVisibilitySample.class);
    /*volatile*/ boolean running = true;

    public void run(){
        log.info("start run");
        while (running){

        }
        log.info("running end");
    }

    /**
     * 正常来说当running为true时，threadA会一直运行，直到running为false。但试着不用volatile修饰running，主线程修改了running，threadA感知不到
     */
    public static void main(String[] args) throws Exception {
        VolatileVisibilitySample sample = new VolatileVisibilitySample();
        Thread threadA = new Thread(sample::run);
        threadA.start();
        TimeUnit.SECONDS.sleep(1);
        sample.running = false;
    }
}
