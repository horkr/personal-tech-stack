package com.horkr.cloud.consumer.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 通过request获取来源标识，交给授权规则进行匹配
 * @author 卢亮宏
 */
@Component
public class CustomRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 这里将origin定义为参数
        String serviceName = request.getParameter("serviceName");
        return serviceName;
    }
}
