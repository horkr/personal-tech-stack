package com.horkr.jdk.learn.network.netty.rpc.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 响应回调管理器
 */
public class ResponseCallBackManager {

    /**
     * key：远程调用请求ID  value：对应回调逻辑
     */
    private static ConcurrentHashMap<Long, Runnable> callBackRegisterMap = new ConcurrentHashMap<>();

    public static void registerCallBack(Long requestId, Runnable callBack) {
        callBackRegisterMap.put(requestId, callBack);
        System.out.println();
    }


    public static Runnable getCallBack(Long requestId) {
        return callBackRegisterMap.get(requestId);
    }
}
