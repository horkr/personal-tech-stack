package com.horkr.spring.learn.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
@Component
public class CustomListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("==========="+event.getName());
        System.out.println("==========="+event.getSource());
    }
}