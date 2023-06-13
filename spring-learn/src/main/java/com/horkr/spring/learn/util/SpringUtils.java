package com.horkr.spring.learn.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author 卢亮宏
 */
@Component
public class SpringUtils  implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static DefaultListableBeanFactory getBeanFactory(){
        return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @return the applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String beanName) {
        if(applicationContext != null && applicationContext.containsBean(beanName)){
            return applicationContext.getBean(beanName);
        }
        return null;
    }

    public static Object getBean(Class classType) {
        if(applicationContext != null){
            return applicationContext.getBean(classType);
        }
        return null;
    }

    public  static <T> Collection<T> getBeansOfAncestor(Class<T> type){
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, type).values();
    }
}
