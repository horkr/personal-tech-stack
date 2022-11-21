package com.horkr.util.load_balance;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author 卢亮宏
 */
public class LoopLoadBalancer<T> extends AbstractLoadBalancer<T> {

    private final AtomicLong atomicLong = new AtomicLong();

    public LoopLoadBalancer(List<T> resources) {
        super(resources);
    }


    @Override
    public T obtainResource() {
        long andIncrement = atomicLong.getAndIncrement();
        return resources.get((int) (andIncrement % resources.size()));
    }
}
