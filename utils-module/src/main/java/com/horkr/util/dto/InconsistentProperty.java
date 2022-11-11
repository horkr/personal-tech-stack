package com.horkr.util.dto;

import java.io.Serializable;

/**
 * 对比后不一致的数据结果
 *
 * @author llh
 */
public class InconsistentProperty implements Serializable {
    private static final long serialVersionUID = -2996016661081460639L;
    /**
     * 属性的key
     */
    private String key;

    /**
     * 原始数据
     */
    private Object oldData;

    /**
     * 新数据
     */
    private Object newData;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getOldData() {
        return oldData;
    }

    public void setOldData(Object oldData) {
        this.oldData = oldData;
    }

    public Object getNewData() {
        return newData;
    }

    public void setNewData(Object newData) {
        this.newData = newData;
    }

    public InconsistentProperty(String key, Object oldData, Object newData) {
        this.key = key;
        this.oldData = oldData;
        this.newData = newData;
    }
}
