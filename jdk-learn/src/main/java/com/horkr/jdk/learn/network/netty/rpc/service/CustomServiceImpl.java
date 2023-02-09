package com.horkr.jdk.learn.network.netty.rpc.service;

import com.horkr.util.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CustomServiceImpl implements CustomService {
    public final Logger log = LoggerFactory.getLogger(CustomServiceImpl.class);

    @Override
    public String update(int num) {
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        log.info("执行了更新--------------------");
        return "server updated: " + num;
    }


}
