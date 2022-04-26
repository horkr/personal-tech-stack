package com.horkr.concurrency.base.volatitle;

import static java.util.Objects.isNull;

/**
 * 1.双重判空原因
 * 多线程情况下，可能俩个线程同时进入第一层判断,对象可能会被创建多次
 * 2.使用volatile的原因
 * 对象创建分为三步
 * a.address = allocate(申请内存空间)
 * b.instance(Object)实例化对象
 * c.myInstance = address
 * 如果发送指令重排,可能A线程执行顺序是a,c,b.那么当B线程执行到synchronized内部时instance已经不是空了,会直接返回一个无实例数据的地址
 */
public class Singleton {
    private volatile static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance(){
        if(isNull(instance)){
            synchronized (Singleton.class){
                if(isNull(instance)){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
