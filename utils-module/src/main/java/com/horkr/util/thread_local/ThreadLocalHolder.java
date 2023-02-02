package com.horkr.util.thread_local;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author 卢亮宏
 */
public class ThreadLocalHolder {

    private static ThreadLocal<Map<String,Object>> local = new ThreadLocal<>();


    /**
     * 放入缓存值
     * @param key   key
     * @param value value
     * @return  T
     */
    public static<T> T put(String key, T value){
        Map<String, Object> mapCache = local.get();
        if(isNull(mapCache)){
            mapCache = new HashMap<>();
            local.set(mapCache);
        }
        mapCache.put(key,value);
        return value;
    }

    /**
     * 获取缓存值
     * @param key   key
     * @return  T
     */
    public static<T> T get(String key){
        Map<String, Object> mapCache = local.get();
        if(isNull(mapCache)){
            return null;
        }
        Object value = mapCache.get(key);
        return isNull(value)?null: (T) value;
    }

    /**
     * 清除threadLocal
     */
    public static void clear() {
        if(nonNull(local)){
            local.remove();
        }
    }
}
