package com.horkr.spring.learn;

import com.horkr.spring.learn.aop.CustomService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 卢亮宏
 */
@SpringBootApplication
public class SpringLearnApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(SpringLearnApplication.class, args);
        CustomService customService = app.getBean(CustomService.class);
        customService.add();
    }


}
