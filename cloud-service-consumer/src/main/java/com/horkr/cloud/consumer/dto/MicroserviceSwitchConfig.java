package com.horkr.cloud.consumer.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 微服务开关配置
 *
 * @author 卢亮宏
 */


public class MicroserviceSwitchConfig {
    @Valid
    private List<MicroserviceAccessDefinition> definitions = new ArrayList<>();


    public List<MicroserviceAccessDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<MicroserviceAccessDefinition> definitions) {
        this.definitions = definitions;
    }

}
