package com.horkr.cloud.consumer.dynamicAop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.Advisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卢亮宏
 */
@Component
public class DynamicAopConfig implements BeanDefinitionRegistryPostProcessor {
    private static final String FEIGN_INVOCATION_HANDLER_PACKAGE = "feign.ReflectiveFeign$FeignInvocationHandler";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNamesForType = beanFactory.getBeanNamesForType(Advisor.class);
        System.out.println();
//        Map<String, Object> beanMap = beanFactory.getBeansWithAnnotation(FeignClient.class);
//        List<String> cutClassList = new ArrayList<>();
//        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (Proxy.isProxyClass(value.getClass()) && FEIGN_INVOCATION_HANDLER_PACKAGE.equals(Proxy.getInvocationHandler(value).getClass().getName())) {
//                cutClassList.add(key);
//            }
//        }
//        String express = cutClassList.stream().map(className -> String.format("target(%s)", className)).collect(Collectors.joining("||"));
//        DynamicAdvisor dynamicAdvisor = new DynamicAdvisor(express, new MethodInterceptor() {
//            @Override
//            public Object invoke(MethodInvocation invocation) throws Throwable {
//                System.err.println("拦截成功");
//                return invocation.proceed();
//            }
//        });
//        beanFactory.registerSingleton("customIntereprtor", dynamicAdvisor);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        List<String> cutClassList = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            String factoryBeanObjectType = (String) beanDefinition.getAttribute("factoryBeanObjectType");
            if (StringUtils.isNotEmpty(factoryBeanObjectType)) {
                cutClassList.add(factoryBeanObjectType);
            }
        }
        String express = cutClassList.stream().map(className -> String.format("bean(%s)", className)).collect(Collectors.joining("||"));
        DynamicAdvisor dynamicAdvisor = new DynamicAdvisor(express, new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                return invocation.proceed();
            }
        });
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) registry;
        factory.registerSingleton("customIntereprtor",dynamicAdvisor);
    }
}
