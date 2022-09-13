package com.horkr.cloud.consumer.service_decoupling;

import cn.hutool.extra.spring.SpringUtil;
import com.horkr.cloud.consumer.dto.MicroserviceAccessDefinition;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * feign接口方法拦截器
 *
 * @author 卢亮宏
 */
public class FeignMethodInterceptor implements MethodInterceptor {
    private final Logger log = LoggerFactory.getLogger(FeignMethodInterceptor.class);
    private Class<? extends MicroserviceSwitchService> microserviceSwitchServiceClass;

    public FeignMethodInterceptor(Class<? extends MicroserviceSwitchService> microserviceSwitchServiceClass) {
        this.microserviceSwitchServiceClass = microserviceSwitchServiceClass;
    }

    private static final String VOID = "void";


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        FeignClient feignClient = declaringClass.getAnnotation(FeignClient.class);
        //  未标记@FeignClient直接执行
        if (isNull(feignClient)) {
            log.warn("类：{}上无FeignClient注解,但却被FeignInterceptor拦截了", declaringClass.getName());
            return invocation.proceed();
        }
        // matrix rule未配置开关直接执行
        String serviceName = StringUtils.isEmpty(feignClient.value()) ? feignClient.name() : feignClient.value();
        MicroserviceSwitchService microserviceSwitchService = SpringUtil.getBean(microserviceSwitchServiceClass);
        Map<String, MicroserviceAccessDefinition> configMap = microserviceSwitchService.obtainConfigMap();
        if (isNull(configMap)) {
            configMap = new HashMap<>();
        }
        MicroserviceAccessDefinition accessDefinition = configMap.get(serviceName);
        if (isNull(accessDefinition)) {
            return emptyValue(method);
        }

        // 非请求类方法（没有@RequestMapping注解）直接执行
        if (!hasRequestAnnotation(method)) {
            return invocation.proceed();
        }
        // 未关闭的直接执行
        if (!accessDefinition.isClosed()) {
            return invocation.proceed();
        }
        // 通过代理执行
        return executeByProxy(accessDefinition, invocation);
    }


    /**
     * 通过代理执行
     *
     * @param accessDefinition 微服务开关配置
     * @param invocation       invocation
     * @return Object
     */
    private Object executeByProxy(MicroserviceAccessDefinition accessDefinition, MethodInvocation invocation) throws Exception {
        Object bean = obtainProxyBean(accessDefinition.getDegradeBeanClassPackage());
        Method method = invocation.getMethod();
        if (nonNull(bean)) {
            Method proxyMethod = ReflectionUtils.findMethod(bean.getClass(), method.getName(), method.getParameterTypes());
            if (isNull(proxyMethod)) {
                log.error("在微服务:{}代理执行bean:{}中未找到方法：{}", accessDefinition.getServiceName(), accessDefinition.getDegradeBeanClassPackage(), method.getName());
                return emptyValue(method);
            } else {
                log.info("方法：{}#{}由降级类：{}执行", method.getDeclaringClass().getSimpleName(),method.getName(), bean.getClass());
                return proxyMethod.invoke(bean, invocation.getArguments());
            }
        } else {
            return emptyValue(method);
        }
    }

    /**
     * 获取代理执行的bean
     *
     * @param proxyBeanClassPackage 代理执行的bean 类路径
     * @return Object
     * @throws Exception
     */
    private Object obtainProxyBean(String proxyBeanClassPackage) {
        if (StringUtils.isEmpty(proxyBeanClassPackage)) {
            return null;
        }
        Class<?> proxyBeanClass;
        try {
            proxyBeanClass = Class.forName(proxyBeanClassPackage);
        } catch (ClassNotFoundException e) {
            log.error("找不到配置的微服务代理执行bean class：{}", proxyBeanClassPackage);
            return null;
        }
        try {
            return SpringUtil.getBean(proxyBeanClass);
        } catch (Exception e) {
            log.error("从Spring容器中获取微服务代理执行bean:{}时出错", proxyBeanClassPackage, e);
            return null;
        }
    }

    /**
     * 返回默认值，空对象或者null
     *
     * @param method method
     * @return Object
     */
    private Object emptyValue(Method method) {
        log.info("方法：{}#{}未在代理bean中执行，返回空值", method.getDeclaringClass().getSimpleName(), method.getName());
        Class<?> returnType = method.getReturnType();
        if (VOID.equals(returnType.getName())) {
            return null;
        }
        Constructor<?> constructor;
        try {
            constructor = returnType.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            // 没有无参构造，返回空
            return null;
        }
    }

    /**
     * 判断方法上是否有 RequestMapping注解
     *
     * @param method method
     * @return boolean
     */
    private boolean hasRequestAnnotation(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType == RequestMapping.class) {
                return true;
            }
            if (annotationType.isAnnotationPresent(RequestMapping.class)) {
                return true;
            }
        }
        return false;
    }
}
