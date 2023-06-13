package com.horkr.spring.learn.sourceCode.lifecycle;


import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
@Component
public class CustomSmartLifecycle implements SmartLifecycle {

    @Override
    public void start() {
        System.out.println("start");
    }

    @Override
    public void stop() {
        System.out.println("stop");
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
