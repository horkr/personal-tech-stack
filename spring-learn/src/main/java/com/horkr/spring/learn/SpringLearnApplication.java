package com.horkr.spring.learn;

import com.horkr.spring.learn.aop.CustomService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 卢亮宏
 */
@EnableSwagger2
@SpringBootApplication
public class SpringLearnApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(SpringLearnApplication.class, args);
        CustomService customService = app.getBean(CustomService.class);
        customService.add();
    }
}
