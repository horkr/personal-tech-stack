package com.horkr.eureka.provider;

import com.horkr.eureka.common.service.CommonFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController implements CommonFeign {

    /**
     * 这里服务提供方提供了一个接口，并注册到eureka中，供夫妇
     */
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "/hiPath",method = RequestMethod.GET)
    @Override
    public String request(String name) {
        return name+":"+port;
    }
}
