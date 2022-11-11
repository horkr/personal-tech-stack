package com.horkr.util.dto;

import java.util.List;

public class TreeCompareParam extends TreeCompareBaseParam {

    public TreeItem getOldTree() {
        return oldTree;
    }

    public void setOldTree(TreeItem oldTree) {
        this.oldTree = oldTree;
    }

    public List<NewCompareTreeParam> getCompareTreeParams() {
        return compareTreeParams;
    }

    public void setCompareTreeParams(List<NewCompareTreeParam> compareTreeParams) {
        this.compareTreeParams = compareTreeParams;
    }

    private static final long serialVersionUID = -5445717175622798893L;
    /**
     * 原始树,即基准树
     */
    private TreeItem oldTree;

    /**
     * 对比树参数
     */
    List<NewCompareTreeParam> compareTreeParams;


    public void check() {
        super.check();
        checkConfig(oldTree,"oldTree");
        checkConfig(compareTreeParams,"compareTreeParams");
    }
}
