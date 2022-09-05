package com.horkr.spring.learn.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author 卢亮宏
 */
public class SpelDemo {
    public static void main(String[] args) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression("#param!=3");
        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("param1","zzzzzzzzz");
        Object value = expression.getValue(context);
        System.out.println(value);
    }
}
