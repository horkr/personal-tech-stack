package com.horkr.cloud.consumer.service_decoupling;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微服务解耦 netflix feign aop代理配置
 *
 * @author 卢亮宏
 */
public class NetflixFeignAopProxyConfig implements BeanDefinitionRegistryPostProcessor {
    private final Logger log = LoggerFactory.getLogger(NetflixFeignAopProxyConfig.class);
    /**
     * 在代理feign时需要使用 MicroserviceSwitchService 获取微服务配置，但MicroserviceSwitchService本身涉及到的微服务不能被代理，否则会导致死循环的代理
     * 如通过matrix rule实现时，使用了pcp-common服务
     */
    private static final Set<String> EXCLUDE_SERVICE_SET = new HashSet<>(Arrays.asList("pcp-common", "pcp-bom-backend"));

    /**
     * feign client工厂类包路径
     */
    private static final String FEIGN_CLIENT_FACTORY_PACKAGE = "org.springframework.cloud.netflix.feign.FeignClientFactoryBean";

    /**
     * 切面表达式pattern
     */
    private static final String EXPRESS_PATTERN = "bean(%s)";
    /**
     * 多切面表达式连接符
     */
    private static final String DELIMITER = "||";

    /**
     * 注册到容器中  通知的beanName
     */
    private static final String NETFLIX_FEIGN_INTERCEPTOR = "netflixFeignInterceptor";

    /**
     * 执行时机:所有的bean定义信息被加载到容器中，Bean实例还没有被初始化
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String express = computeExpress(registry);
        if (StringUtils.isNotEmpty(express)) {
            // 组装aop通知bean,这里不能用IOC容器中的bean，因为这个时候还没用开始初始化，注入的bean将会是空的
            // TODO 如果要使用需要实现MicroserviceSwitchService接口
            DynamicAdvisor dynamicAdvisor = new DynamicAdvisor(express, new FeignMethodInterceptor(MicroserviceSwitchService.class));
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) registry;
            // 注册到IOC容器
            factory.registerSingleton(NETFLIX_FEIGN_INTERCEPTOR, dynamicAdvisor);
        }
    }

    /**
     * 计算拦截feign的切面表达式
     *
     * @param registry registry
     * @return String
     */
    private String computeExpress(BeanDefinitionRegistry registry) {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        Set<String> feignBeanNameSet = new HashSet<>();
        Set<String> serviceNameSet = new HashSet<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            // 只拦截由FeignClientFactoryBean生产的bean
            if (!FEIGN_CLIENT_FACTORY_PACKAGE.equals(beanDefinition.getBeanClassName())) {
                continue;
            }
            Class<?> beanClass;
            try {
                beanClass = Class.forName(beanDefinitionName);
            } catch (ClassNotFoundException e) {
                continue;
            }
            FeignClient feignClient = beanClass.getAnnotation(FeignClient.class);
            String serviceName = StringUtils.isEmpty(feignClient.value()) ? feignClient.name() : feignClient.value();
            // 去除不可代理的bean
            if (!EXCLUDE_SERVICE_SET.contains(serviceName)) {
                feignBeanNameSet.add(beanDefinitionName);
                serviceNameSet.add(serviceName);
            }
        }
        if (feignBeanNameSet.isEmpty()) {
            return StringUtils.EMPTY;
        }
        // 组合aop 切面表达式
        String express = feignBeanNameSet.stream().map(className -> String.format(EXPRESS_PATTERN, className)).collect(Collectors.joining(DELIMITER));
        log.info("微服务解耦：feign代理切面:{}", express);
        log.info("微服务解耦：被代理的微服务：{}", String.join(",", serviceNameSet));
        return express;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
