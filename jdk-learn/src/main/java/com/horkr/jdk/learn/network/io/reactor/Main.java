package com.horkr.jdk.learn.network.io.reactor;

/**
 * @author 卢亮宏
 */
public class Main {

    public static void main(String[] args) {
        NioThreadGroup bossGroup = new NioThreadGroup(1,"boss");
        NioThreadGroup workerGroup = new NioThreadGroup(3,"worker");
        bossGroup.setWorkerGroup(workerGroup);
        bossGroup.createServer(9090);
    }
}
