package com.horkr.spring.learn.lock;

public interface Lock<T> {


    /**
     * 加锁并获取锁节点
     *
     * @return T
     */
    T lock(Long timeOut);

    /**
     * 解锁
     *
     * @param lockNode 锁节点
     */
    void unlock(T lockNode);
}
