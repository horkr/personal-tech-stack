package com.horkr.eureka.common.config;

import com.horkr.eureka.common.interceptor.CommonFeignInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class CommonFeignConfig {

    // 需要配置客户端服务配置yml中的日志
    @Bean
    public Logger.Level level(){
        // 生产建议用basic，否则日志太多
        return Logger.Level.BASIC;
    }


    @Bean
    public RequestInterceptor requestInterceptor(){
        return new CommonFeignInterceptor();
    }

}
