package com.horkr.cloud.consumer.feign_wrap;

import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 卢亮宏
 */
public class WrapFeign extends Feign {
    public static final class Builder extends Feign.Builder {
        @Override
        public Feign build() {
            new InvocationHandlerFactory() {
                @Override
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    return null;
                }
            };
//            invocationHandlerFactory();
            return super.build();
        }
    }

    @Override
    public <T> T newInstance(Target<T> target) {
        return null;
    }
}


