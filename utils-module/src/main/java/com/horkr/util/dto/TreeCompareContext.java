package com.horkr.util.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TreeCompareContext {
    /**
     * 原始树
     */
    private TreeItem oldTree;
    /**
     * 当前对比的树
     */
    private TreeItem newTree;

    /**
     * 是否对比根节点
     */
    private boolean compareRoot;

    /**
     * 单个树节点唯一主键字段
     */
    private List<String> uniqueKeyFields;


    /**
     * 单个树节点对比的属性
     */
    private List<String> compareProperties;

    /**
     * 原始树所有节点的map
     */
    private Map<String, TreeItem> oldTreeNodeMap;

    /**
     * 结果树
     */
    private ComparedTreeItem resultTree;

    /**
     * 唯一主键映射，对比时会将有映射关系的主键视为同一主键
     */
    private Map<String,String> uniqueKeyMapping;

    /**
     * 当前对比树在所有对比树中的索引
     */
    private int treeIndex;

    /**
     * 当前对比树所有节点的对比结果
     */
    private Map<String, TreeNodeCompareChangeType> allNodeCompareChangeTypeMap;

    private Map<String, TreeItem> newTreeNodeMap;

    private Map<String,TreeItem> supplementaryTreeNodeMap;
}
