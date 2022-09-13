package com.horkr.cloud.consumer.service_decoupling;


import com.horkr.cloud.consumer.dto.MicroserviceAccessDefinition;

import java.util.Map;

/**
 * 微服务开关配置服务
 *
 * @author 卢亮宏
 */
@FunctionalInterface
public interface MicroserviceSwitchService {

    /**
     * 获取微服务开关配置
     *
     * @return Map<String, MicroserviceSwitchConfig>
     */
    Map<String, MicroserviceAccessDefinition> obtainConfigMap();
}
