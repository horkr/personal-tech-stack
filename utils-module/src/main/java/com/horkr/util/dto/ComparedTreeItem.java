package com.horkr.util.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * 对比树节点
 */
@Getter
@Setter
public class ComparedTreeItem extends TreeItem {

    private static final long serialVersionUID = -5159499079725879623L;

    public static ComparedTreeItem createFromTreeItem(TreeItem treeItem) {
        ComparedTreeItem comparedTreeItem = new ComparedTreeItem();
        try {
            BeanUtils.copyProperties(comparedTreeItem, treeItem);
        } catch (Exception e) {
            throw new IllegalStateException("can not copy TreeItem to ComparedTreeItem",e);
        }
        return comparedTreeItem;
    }

    /**
     * 重载获取data方法，data不可为空
     *
     * @return Object
     */
    @Override
    public Object getData() {
        Object data = super.getData();
        if (isNull(data)) {
            throw new IllegalStateException("compare node data can't be null");
        }
        return data;
    }


    /**
     * 树节点对比结果。与对比树顺序一致
     */
    private List<TreeNodeCompareData> treeNodeCompareDataList = new ArrayList<>();


    /**
     * 添加树节点对比结果
     *
     * @param treeNodeCompareData treeNodeCompareData
     */
    public void addTreeNodeCompareData(TreeNodeCompareData treeNodeCompareData) {
        this.treeNodeCompareDataList.add(treeNodeCompareData);
    }


    /**
     * 添加树节点对比结果
     *
     * @param treeNodeCompareData treeNodeCompareData
     */
    public void addTreeNodeCompareData(TreeNodeCompareData treeNodeCompareData, int treeIndex) {
        int size = treeNodeCompareDataList.size();
        if (size < treeIndex) {
            treeNodeCompareDataList.add(TreeNodeCompareData.unKnowResult());
        }
        this.treeNodeCompareDataList.add(treeNodeCompareData);
    }

    public void clearCompareData() {
        this.treeNodeCompareDataList.clear();
    }
}
