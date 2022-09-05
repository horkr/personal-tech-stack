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
}
