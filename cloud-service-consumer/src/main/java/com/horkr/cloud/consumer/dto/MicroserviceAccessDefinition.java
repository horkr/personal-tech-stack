package com.horkr.cloud.consumer.dto;

/**
 * @author 卢亮宏
 */
public class MicroserviceAccessDefinition {
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getDegradeBeanClassPackage() {
        return degradeBeanClassPackage;
    }

    public void setDegradeBeanClassPackage(String degradeBeanClassPackage) {
        this.degradeBeanClassPackage = degradeBeanClassPackage;
    }

    /**
     * 服务名
     */
    private String serviceName;


    /**
     * 是否关闭
     */
    private boolean closed;


    /**
     * 降级执行的bean 类路径
     */
    private String degradeBeanClassPackage;
}
