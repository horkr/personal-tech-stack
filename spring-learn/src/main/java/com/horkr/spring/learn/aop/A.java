package com.horkr.spring.learn.aop;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class A {
    @Resource
    private CustomService customService;


}
