package com.horkr.util.load_balance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author 卢亮宏
 */
public class RandomLoadBalancer<T> extends AbstractLoadBalancer<T> {

    public RandomLoadBalancer(List<T> resources) {
        super(resources);
    }

//    static {
//        serverList.add("192.168.1.2");
//        serverList.add("192.168.1.3");
//        serverList.add("192.168.1.4");
//        serverList.add("192.168.1.5");
//    }

    /**
     * 随机路由算法
     */
    public T obtainResource() {
        // 随机数随机访问
        int randomInt = new Random().nextInt(resources.size());
        return resources.get(randomInt);
    }

}
