package com.horkr.util.load_balance;

/**
 * @author 卢亮宏
 */
public interface LoadBalancer<T> {

    T obtainResource();
}
