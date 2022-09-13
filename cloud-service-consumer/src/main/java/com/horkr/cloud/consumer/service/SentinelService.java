package com.horkr.cloud.consumer.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.horkr.cloud.consumer.sentinel.ExceptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
