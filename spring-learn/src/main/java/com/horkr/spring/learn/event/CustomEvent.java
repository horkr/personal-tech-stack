package com.horkr.spring.learn.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author 卢亮宏
 */
public class CustomEvent  extends ApplicationEvent {
    private static final long serialVersionUID = 7099057708183571947L;

    private String name = "horkr";

    public CustomEvent(Object source) {
        super(source);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
