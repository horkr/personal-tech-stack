package com.horkr.util.load_balance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 加权轮询，内置了轮询负载均衡实现
 *
 * @author 卢亮宏
 */
public class LoopWithWeightLoadBalancer<T extends WeightResource> extends AbstractLoadBalancer<T> {

    public LoopWithWeightLoadBalancer(List<T> resources) {
        super(resources);
    }

    private LoopLoadBalancer<T> loopLoadBalancer;

    @Override
    public void loadResource(Collection<T> resources) {
        super.loadResource(resources);
        List<T> newResources = new ArrayList<>();
        for (T resource : super.resources) {
            int weight = resource.getWeight();
            for (int i = 0; i < weight; i++) {
                newResources.add(resource);
            }
        }
        loopLoadBalancer = new LoopLoadBalancer<>(newResources);
    }

    @Override
    public T obtainResource() {
        return loopLoadBalancer.obtainResource();
    }
}
