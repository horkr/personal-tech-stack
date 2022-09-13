package com.horkr.cloud.consumer.controller;

import com.horkr.cloud.consumer.service.ConsumerService;
import com.horkr.cloud.eureka.common.dto.BusinessDto;
import com.horkr.cloud.eureka.common.service.CommonFeign;
import com.horkr.cloud.eureka.common.service.CommonFeignHystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@RefreshScope
@RestController
@RequestMapping("/consume")
public class ConsumerController {
    @Autowired
    ConsumerService consumerService;
    @Autowired
    @Qualifier("com.horkr.cloud.eureka.common.service.CommonFeign")
    CommonFeign commonFeign;

    @RequestMapping(value = "/hi-restemplate")
    public String hi(@RequestParam String name){
        return consumerService.hiService(name);
    }

    @Value("${custom.turnon}")
    private String turnon;

    @Value("${spring.cloud.nacos.discovery.ephemeral}")
    private String ephemeral;
    @RequestMapping(value = "/hi-feign")
    public String hiFeign(@RequestParam String name){
        return commonFeign.request(name);
    }



    @RequestMapping(value = "/obtainConfig")
    public String hello(){
        return turnon+"-"+ephemeral;
    }
}
