package com.horkr.jdk.learn.network.io.reactor;

/**
 * @author 卢亮宏
 */
public class Main {

    public static void main(String[] args) {
        NioThreadGroup nioThreadGroup = new NioThreadGroup(5);
        nioThreadGroup.createServer(9090);
    }
}
