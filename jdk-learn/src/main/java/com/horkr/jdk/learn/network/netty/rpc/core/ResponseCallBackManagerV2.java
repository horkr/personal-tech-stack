package com.horkr.jdk.learn.network.netty.rpc.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 响应回调管理器
 */
public class ResponseCallBackManagerV2 {

    /**
     * key：远程调用请求ID  value：对应回调逻辑
     */
    private static ConcurrentHashMap<Long, CompletableFuture<String>> callBackRegisterMap = new ConcurrentHashMap<>();

    public static void registerCallBack(Long requestId, CompletableFuture<String> future) {
        callBackRegisterMap.put(requestId, future);
    }


    public static void runCallBack(Long requestId, String response) {
        CompletableFuture<String> future = callBackRegisterMap.get(requestId);
        future.complete(response);
        callBackRegisterMap.remove(requestId);
    }
}
