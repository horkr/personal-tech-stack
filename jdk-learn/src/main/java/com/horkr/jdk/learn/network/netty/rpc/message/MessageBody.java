package com.horkr.jdk.learn.network.netty.rpc.message;

import java.io.Serializable;

/**
 * RPC通信消息体
 *
 * @author 卢亮宏
 */
public class MessageBody implements Serializable {
    /**
     * 调用的接口名称
     */
    private String interfaceName;

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 参数集合
     */
    private Object[] args;

    /**
     * 方法调用结果
     */
    private Object response;


    public MessageBody() {
    }

    public MessageBody(String interfaceName, String methodName, Class<?>[] paramTypes, Object[] args, Object response) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.args = args;
        this.response = response;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
