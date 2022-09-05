package com.horkr.cloud.consumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.horkr.cloud.consumer.sentinel.ExceptionUtil;
import com.horkr.cloud.consumer.service.SentinelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 卢亮宏
 */
@RestController
public class SentinelAnnotationDemoController {
    private static final String RESOURCE_NAME = "hello";



    /**
     * 测试直接流控qps
     */
    @SentinelResource(value = RESOURCE_NAME,
            fallback = "fallback", fallbackClass = ExceptionUtil.class,
            blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class
    )
    @GetMapping(value = "/hello")
    public String hello() {
        return "hello world";
    }


    /**
     * -----------------------------------------测试链路流控qps-----------------------------------------------------------
     */
    @Resource
    private SentinelService service;
    @GetMapping(value = "/linkTest1")
    public String linkTest1() {
        return service.linkTest();
    }


    @GetMapping(value = "/linkTest2")
    public String linkTest2() {
        return service.linkTest();
    }





    /**
     * -----------------------------------------熔断降级测试-----------------------------------------------------------
     */








}
