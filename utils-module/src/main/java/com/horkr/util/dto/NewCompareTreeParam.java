package com.horkr.util.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NewCompareTreeParam {

    /**
     * 对比树
     */
    private TreeItem newTree;

    /**
     * 补充节点（用于判断节点自身是否为新增，无此需求可忽略此参数）
     */
    private List<TreeItem> supplementaryTreeNodes = new ArrayList<>();
}
