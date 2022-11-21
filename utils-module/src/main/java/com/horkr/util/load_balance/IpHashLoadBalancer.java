package com.horkr.util.load_balance;

import java.util.List;

/**
 * @author 卢亮宏
 */
public class IpHashLoadBalancer<T extends IpResource> extends AbstractLoadBalancer<T> {

    public IpHashLoadBalancer(List<T> resources) {
        super(resources);
    }

    @Override
    public T obtainResource() {
        //
        throw new UnsupportedOperationException("");
    }


    public T obtainResource(String ip) {
        int index = ip.hashCode() % resources.size();
        return resources.get(index);
    }
}
