package com.horkr.util.dto;


import java.util.ArrayList;
import java.util.List;

public class NewCompareTreeParam {

    /**
     * 对比树
     */
    private TreeItem newTree;

    /**
     * 补充节点（用于判断节点自身是否为新增，无此需求可忽略此参数）
     */
    private List<TreeItem> supplementaryTreeNodes = new ArrayList<>();


    public TreeItem getNewTree() {
        return newTree;
    }

    public void setNewTree(TreeItem newTree) {
        this.newTree = newTree;
    }

    public List<TreeItem> getSupplementaryTreeNodes() {
        return supplementaryTreeNodes;
    }

    public void setSupplementaryTreeNodes(List<TreeItem> supplementaryTreeNodes) {
        this.supplementaryTreeNodes = supplementaryTreeNodes;
    }
}
