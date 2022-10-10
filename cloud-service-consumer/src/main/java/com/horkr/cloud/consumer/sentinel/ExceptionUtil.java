package com.horkr.cloud.consumer.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author 卢亮宏
 */
public class ExceptionUtil {
    public static String fallback(Throwable e){
        return "===被异常降级啦===";
    }

    public static String handleException(BlockException e){
        return "===被限流啦===";
    }


    /**
     * 参数qps限流处理
     * @param e
     * @return
     */
    public static String paramQpsHandleException(int id,BlockException e){
        return id+"===被限流啦===";
    }


    /**
     * 参数qps限流处理
     */
    public static String authRuleHandleException(String serviceName,BlockException e){
        return serviceName+"===被限流啦===";
    }
}
