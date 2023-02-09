package com.horkr.spring.learn.controller;

import com.horkr.algorithm.limit_request.LeakyBucket;
import com.horkr.algorithm.limit_request.SlidingTimeWindowLimiter;
import com.horkr.spring.learn.event.CustomEvent;
import com.horkr.spring.learn.messagesource.MessageUtil;
import com.horkr.spring.learn.messagesource.MsgConstants;
import com.horkr.spring.learn.messagesource.PatternMessageSource;
import com.horkr.spring.learn.transaction.PeopleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Resource
    private PeopleService peopleService;

    SlidingTimeWindowLimiter slidingTimeWindowLimiter = new SlidingTimeWindowLimiter(10, 5, 1000);

    LeakyBucket leakyBucket = new LeakyBucket(2);


    @GetMapping("/test-spring-event")
    public Object request() {
        context.publishEvent(new CustomEvent("测试"));
        return null;
    }

    @GetMapping("/data")
    public Object data() {
        return "some thing return";
    }


    @GetMapping("/test")
    public Object test() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        return MessageUtil.getMessage(MsgConstants.FILE_ONLY);
    }

    @GetMapping("/timeWindowTest")
    public Object timeWindowTest() throws Exception {
        if (!slidingTimeWindowLimiter.canPass()) {
            throw new IllegalStateException("block");
        }
        Thread.sleep(5);
        return "pass";
    }

    @GetMapping("/leakyBucketTest")
    public Object leakyBucketTest() throws Exception {
        if (leakyBucket.canPass()) {
            double qpsDoubleValue = Double.parseDouble(String.valueOf(2000));
            double speed = 1 / qpsDoubleValue;
            double time = speed * 1000;
            return time + "::" + (long) time;
        } else {
            throw new IllegalStateException("block");
        }

    }

    @GetMapping("/queryPeople")
    public Object queryPeople() {
        return peopleService.queryAll();
    }

}
