package com.horkr.jdk.learn.concurrency.timeout;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author 卢亮宏
 */
public class TimeoutUtil {

    public static void runWithTimeout(long time, TimeUnit unit, Runnable runnable){
        try {
            CompletableFuture.runAsync(runnable).get(time,unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
