package com.horkr.util.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 树节点对比数据
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TreeNodeCompareData implements Serializable {

    private static final long serialVersionUID = -7077329946709075931L;

    public static TreeNodeCompareData unKnowResult() {
        return new TreeNodeCompareData(TreeNodeCompareChangeType.UN_KNOW, TreeNodeCompareChangeBody.UN_KNOW, Collections.emptyList());
    }

    /**
     * 变更类型
     */
    private TreeNodeCompareChangeType compareChangeType;

    /**
     * 变更主体
     */
    private TreeNodeCompareChangeBody compareChangeBody;

    /**
     * 变更属性
     */
    private List<InconsistentProperty> inconsistentProperties;
}
