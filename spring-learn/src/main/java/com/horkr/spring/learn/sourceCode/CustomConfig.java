package com.horkr.spring.learn.sourceCode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 卢亮宏
 */
@Configuration
@ComponentScan(basePackages = {"com.horkr.spring"})
public class CustomConfig {

    @Bean
    public TestBean2 create(){
        return new TestBean2();
    }
}
