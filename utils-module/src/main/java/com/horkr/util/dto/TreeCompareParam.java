package com.horkr.util.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TreeCompareParam extends TreeCompareBaseParam {

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
