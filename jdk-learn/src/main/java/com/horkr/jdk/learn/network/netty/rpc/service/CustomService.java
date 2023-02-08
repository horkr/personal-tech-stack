package com.horkr.jdk.learn.network.netty.rpc.service;

import com.horkr.jdk.learn.network.netty.rpc.bo.CustomBo;

public interface CustomService {

    void update(int num);

    CustomBo query(String id);
}
