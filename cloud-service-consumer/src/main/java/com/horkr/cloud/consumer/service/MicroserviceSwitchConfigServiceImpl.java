package com.horkr.cloud.consumer.service;

import com.horkr.cloud.consumer.dto.MicroserviceAccessDefinition;
import com.horkr.cloud.consumer.dto.MicroserviceSwitchConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 微服务开关配置服务 配置文件方式实现
 * @author 卢亮宏
 */
public class MicroserviceSwitchConfigServiceImpl implements MicroserviceSwitchService {

    private final Map<String, MicroserviceAccessDefinition> configMap;

    public MicroserviceSwitchConfigServiceImpl(MicroserviceSwitchConfig microserviceSwitchConfig) {
        List<MicroserviceAccessDefinition> definitions = microserviceSwitchConfig.getDefinitions();
        this.configMap = definitions.stream().collect(Collectors.toMap(MicroserviceAccessDefinition::getServiceName, Function.identity(),(a, b)->a));
    }

    @Override
    public Map<String, MicroserviceAccessDefinition> obtainConfigMap(){
        return configMap;
    }
}
