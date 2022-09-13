package com.horkr.cloud.consumer.dynamicAop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import static java.util.Objects.isNull;

/**
 * 动态增强器
 *
 * @author lulianghong
 */
public class DynamicAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    /**
     * 表达式
     */
    private String expression;
    private AspectJExpressionPointcut pointcut;

    public DynamicAdvisor(String expression, Advice advice) {
        this.expression = expression;
        this.setAdvice(advice);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }

    @Override
    public Pointcut getPointcut() {
        if (isNull(pointcut)) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(this.expression);
            return pointcut;
        } else {
            return pointcut;
        }
    }
}
