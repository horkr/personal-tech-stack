package com.horkr.cloud.eureka.common.service;

import com.horkr.cloud.eureka.common.dto.BusinessDto;
import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
@Component
public class CommonFeignHystrix implements CommonFeign{

    public String request(String name) {
        return "已熔断";
    }

    public BusinessDto testInterceptFeign(){
        return new BusinessDto("c","d");
    }
}
