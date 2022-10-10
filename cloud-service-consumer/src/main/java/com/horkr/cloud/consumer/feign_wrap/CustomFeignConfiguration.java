package com.horkr.cloud.consumer.feign_wrap;

import com.alibaba.cloud.sentinel.feign.SentinelFeign;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import feign.Feign;

/**
 * 模拟sentinel 可以修改feign的代理执行逻辑
 *
 * @author 卢亮宏
 * @see SentinelFeignAutoConfiguration
 */
public class CustomFeignConfiguration {


    // 当这个配置为true时生效
//    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder customSentinelBuilder() {
        return SentinelFeign.builder();
    }
}
