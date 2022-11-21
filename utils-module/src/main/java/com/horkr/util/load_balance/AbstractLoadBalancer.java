package com.horkr.util.load_balance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 卢亮宏
 */
public abstract class AbstractLoadBalancer<T> implements LoadBalancer<T> {

    protected List<T> resources;

    public AbstractLoadBalancer(List<T> resources) {
        loadResource(resources);
    }

    public void loadResource(Collection<T> resources) {
        this.resources = Collections.unmodifiableList(new ArrayList<>(resources));
    }
}
