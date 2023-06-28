package com.horkr.spring.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class DemoController {

    @GetMapping("/test/xxx")
    public String dd(){
        System.out.println(new Date());
        return "xxx";
    }
}
