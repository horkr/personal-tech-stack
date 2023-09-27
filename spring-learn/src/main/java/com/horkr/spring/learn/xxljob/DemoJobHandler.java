package com.horkr.spring.learn.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DemoJobHandler {
    private static final Logger log = LogManager.getLogger(DemoJobHandler.class);
    @XxlJob("demoJobHandler")
    public Object demo(){
        log.info("已执行---------");
        return null;
    }
}
