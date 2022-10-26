package com.horkr.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static void sleep(TimeUnit timeUnit,long time){
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ignore) {
            // ignore
        }
    }

    public static void startByNewThread(Runnable runnable){
        new Thread(runnable).start();

    }

}
