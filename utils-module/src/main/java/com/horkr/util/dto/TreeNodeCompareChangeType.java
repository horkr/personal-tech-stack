package com.horkr.util.dto;

/**
 * 树节点对比变更类型
 */
public enum TreeNodeCompareChangeType {
    /**
     * 新增
     */
    NEW,
    /**
     * 修改
     */
    UPDATE,
    /**
     * 无变化
     */
    NO_CHANGE,
    /**
     * 移除
     */
    REMOVE,
    /**
     * 升版
     */
    REVISE,
    /**
     * 未知
     */
    UN_KNOW;
}
