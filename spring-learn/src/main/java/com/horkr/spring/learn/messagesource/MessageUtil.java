package com.horkr.spring.learn.messagesource;

import com.horkr.spring.learn.util.SpringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Objects;

/**
 * @author 卢亮宏
 */
public class MessageUtil {
    private static MessageSourceAccessor fileMessageSourceAccessor;
    private static MessageSourceAccessor dbMessageSourceAccessor;


    public static MessageSourceAccessor dbMessageSourceAccessor() {
        if (dbMessageSourceAccessor == null) {
            dbMessageSourceAccessor = new MessageSourceAccessor((MessageSource) SpringUtils.getBean(DbMessageSource.class));
        }
        return dbMessageSourceAccessor;
    }

    public static final String getMessage(String code, String... args) {
        MessageSourceAccessor dbMessageSourceAccessor = dbMessageSourceAccessor();
        return dbMessageSourceAccessor.getMessage(code, args, code);

    }

    /**
     * 获取国际化信息
     *
     * @param note 中文注释
     * @param code 国际化code
     * @param args 国际化参数
     * @return 消息
     */
    public static final String getMsgAndNote(String note, String code, String... args) {
        return getMessage(note, args);
    }
}
