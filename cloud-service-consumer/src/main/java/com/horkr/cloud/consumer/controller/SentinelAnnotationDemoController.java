package com.horkr.cloud.consumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.horkr.cloud.consumer.sentinel.ExceptionUtil;
import com.horkr.cloud.consumer.service.SentinelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 卢亮宏
 */
@RestController
public class SentinelAnnotationDemoController {
    private static final String RESOURCE_NAME = "hello";
    private static final String SUCCESS = "success!";


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


    /**
     * 测试慢调用比例熔断
     */
    @SentinelResource(value = "slowCallRatio",
            fallback = "fallback", fallbackClass = ExceptionUtil.class,
            blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class
    )
    @GetMapping(value = "/slowCallRatio")
    public String slowCallRatio() throws InterruptedException {
        Thread.sleep(100);
        return SUCCESS;
    }






    /**
     * 测试异常比例熔断
     */
    private static AtomicInteger callCount = new AtomicInteger();
    @SentinelResource(value = "errorRatio",
//            fallback = "fallback", fallbackClass = ExceptionUtil.class,
            blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class
    )
    @GetMapping(value = "/errorRatio")
    public String errorRatio(){
        if(callCount.getAndIncrement()%2==0){
            return SUCCESS;
        }else {
            throw new IllegalStateException();
        }
    }



    /**
     * 参数QPS熔断
     */
    @SentinelResource(value = "paramQps",
            fallback = "fallback", fallbackClass = ExceptionUtil.class,
            blockHandler = "paramQpsHandleException", blockHandlerClass = ExceptionUtil.class
    )
    @GetMapping(value = "/paramQps/{id}")
    public String paramQps(@PathVariable("id") Integer id){
        return id+SUCCESS;
    }



    /**
     * 授权规则测试
     */
    @SentinelResource(value = "authRule",
            fallback = "fallback", fallbackClass = ExceptionUtil.class,
            blockHandler = "authRuleHandleException", blockHandlerClass = ExceptionUtil.class
    )
    @GetMapping(value = "/authRule")
    public String authRule(@RequestParam("serviceName") String serviceName){
        return serviceName+SUCCESS;
    }

}
