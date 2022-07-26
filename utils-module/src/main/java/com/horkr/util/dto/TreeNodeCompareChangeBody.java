package com.horkr.util.dto;

/**
 * 树节点对比变更主体
 */
public enum TreeNodeCompareChangeBody {
    /**
     * 关系和节点
     */
    RELATION_AND_NOE,
    /**
     * 仅关系变更
     */
    RELATION,
    /**
     * 仅节点变更
     */
    NODE,

    /**
     * 未知
     */
    UN_KNOW
}
