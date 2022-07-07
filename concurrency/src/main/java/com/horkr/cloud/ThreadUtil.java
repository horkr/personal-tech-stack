package com.horkr.cloud;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static void sleep(TimeUnit timeUnit,int time){
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startByNewThread(Runnable runnable){
        new Thread(runnable).start();
    }
}
