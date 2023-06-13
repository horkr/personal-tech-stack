package com.horkr.spring.learn.messagesource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 卢亮宏
 */
@Configuration
public class MessageSourceConfiguration {

    @Bean("messageSource")
    public PatternMessageSource patternMessageSource() {
        PatternMessageSource msgSource = new PatternMessageSource();
        String[] basenames = new String[]{"classpath*:i18n/messages", "classpath*:i18n/res", "classpath*:i18n/prod"};
        msgSource.setBasenames(basenames);
        msgSource.setDefaultEncoding("UTF-8");
        return msgSource;
    }

}
