package com.horkr.cloud.consumer.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author 卢亮宏
 */
@Service
public class SentinelService {
    @SentinelResource(value = "linkTest",
            fallback = "fallback"
    )
    public String linkTest() {

        return "hello world";
    }
}
