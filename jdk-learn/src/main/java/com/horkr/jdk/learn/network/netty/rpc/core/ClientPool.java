package com.horkr.jdk.learn.network.netty.rpc.core;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 卢亮宏
 */
public class ClientPool {

    private int poolSize;

    private List<NioSocketChannel> pool;

    private List<Object> locks;

    public NioSocketChannel getClient(int index) {
        return pool.get(index);
    }


    public ClientPool(int poolSize) {
        this.poolSize = poolSize;
        this.pool = new ArrayList<>(poolSize);
        this.locks =  new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            pool.add(null);
        }
        for (int i = 0; i < poolSize; i++) {
            locks.add(new Object());
        }
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public List<NioSocketChannel> getPool() {
        return pool;
    }

    public void setPool(List<NioSocketChannel> pool) {
        this.pool = pool;
    }

    public List<Object> getLocks() {
        return locks;
    }

    public void setLocks(List<Object> locks) {
        this.locks = locks;
    }
}
