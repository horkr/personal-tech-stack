package com.horkr.spring.learn.sourceCode.lifecycle;


import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
@Component
public class CustomLifecycle implements Lifecycle {
    /**
     * 运行状态
     */
    private volatile boolean running = false;

    /**
     * 容器启动后调用
     */
    @Override
    public void start() {
        System.out.println("容器启动后执行MyLifeCycle操作...");
        running = true;
    }

    /**
     * 容器停止时调用
     */
    @Override
    public void stop() {
        System.out.println("收到关闭容器的信号MyLifeCycle操作...");
        running = false;
    }

    /**
     * 检查此组件是否正在运行。
     * 1. 只有该方法返回false时，start方法才会被执行。
     * 2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
     */
    @Override
    public boolean isRunning() {
        System.out.println("检查MyLifeCycle组件的运行状态：" + running);
        return running;
    }
}
