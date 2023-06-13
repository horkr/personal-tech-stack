package com.horkr.spring.learn.messagesource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

/**
 * @author 卢亮宏
 */
@Component
public class DbMessageSource extends AbstractMessageSource implements ResourceLoaderAware {
    // 这个是用来缓存数据库中获取到的配置的 数据库配置更改的时候可以调用reload方法重新加载
    private static  Map<String, Map<String, String>> LOCAL_CACHE = new ConcurrentHashMap<>(256);
    ResourceLoader resourceLoader;
    @Resource
    @Qualifier("messageSource")
    PatternMessageSource patternMessageSource;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader == null ? new DefaultResourceLoader() : resourceLoader);
    }


    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getSourceFromCache(code, locale);
        return new MessageFormat(msg, locale);
    }


    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return getSourceFromCache(code, locale);
    }


    @PostConstruct
    public void init() {
        this.reload();
        this.setParentMessageSource(patternMessageSource);
    }

    public void reload() {
        Map<String, Map<String, String>> reloadMap = loadAllMessageResourcesFromDB();
        LOCAL_CACHE = new ConcurrentHashMap<>(reloadMap);
    }

    /**
     * TODO 改为从DB获取
     * @return
     */
    public Map<String, Map<String, String>> loadAllMessageResourcesFromDB() {
        // 获取数据库配置
        Map<String, Map<String, String>> messageMap = new HashMap<>();
        Map<String, String> chMessageMap = new HashMap<>();
        chMessageMap.put("msg.demo", "数据库测试{0}");
        Map<String, String> enMessageMap = new HashMap<>();
        enMessageMap.put("msg.demo", "db demo{0}");
        // 加入缓存
        messageMap.put("zh", chMessageMap);
        messageMap.put("en", enMessageMap);
        return messageMap;
    }

    public String getSourceFromCache(String code, Locale locale) {
        // 获取缓存中对应语言的所有数据项
        Map<String, String> props = LOCAL_CACHE.get(locale.getLanguage());
        return nonNull(props) ? props.get(code) : null;
    }
}
