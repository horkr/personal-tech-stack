package com.horkr.spring.learn.controller;

import com.horkr.spring.learn.event.CustomEvent;
import com.horkr.spring.learn.messagesource.MessageUtil;
import com.horkr.spring.learn.messagesource.MsgConstants;
import com.horkr.spring.learn.messagesource.PatternMessageSource;
import com.horkr.spring.learn.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author 卢亮宏
 */
@RestController
public class TestController {

    @Resource
    private ApplicationContext context;

    @Resource
    private PatternMessageSource messageSource;

    @GetMapping("/test-spring-event")
    public Object request() {
        context.publishEvent(new CustomEvent("测试"));
        return null;
    }


    @GetMapping("/test")
    public Object test() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        return MessageUtil.getMessage(MsgConstants.FILE_ONLY);
    }
}
