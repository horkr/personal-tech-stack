package com.horkr.util.trace;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卢亮宏
 */
public class StackTraceUtil {

    public static List<String> getCallChain() {
        List<String> chain = new ArrayList<>();
        StackTraceElement[] stackTraceArr = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceArr) {
            String className = stackTraceElement.getClassName();
            if(className.startsWith("com.horkr")){

            }
            String methodInfo = JSON.toJSONString(stackTraceElement);
            chain.add(methodInfo);
        }
        return chain;
    }
}
