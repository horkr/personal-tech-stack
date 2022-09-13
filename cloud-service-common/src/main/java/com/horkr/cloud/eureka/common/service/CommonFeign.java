package com.horkr.cloud.eureka.common.service;

import com.horkr.cloud.eureka.common.config.CommonFeignConfig;
import com.horkr.cloud.eureka.common.dto.BusinessDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-provider",configuration = CommonFeignConfig.class)
public interface CommonFeign {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String request(@RequestParam("name") String name);


    /**
     * 测试拦截feign
     * @return  BusinessDto
     */
    @PostMapping("zzz")
    default BusinessDto testInterceptFeign(){
        return new BusinessDto("a","b");
    }
    /**
     * 测试拦截feign
     * @return  BusinessDto
     */
    default void testInterceptFeignNoRequestMapping(){

    }
}
