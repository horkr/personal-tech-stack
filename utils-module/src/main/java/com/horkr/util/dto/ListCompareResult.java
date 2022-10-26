package com.horkr.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 两个list对比结果
 * @author llh
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListCompareResult<T> {

    /**
     * 新增的数据
     */
    private List<T> addList;

    /**
     * 移除的数据
     */
    private List<T> removeList;

    /**
     * 无变化的数据
     */
    private List<T> noChangeList;

    /**
     * 更新的数据
     */
    private List<T> changeList;

    /**
     * 替换的数据
     */
    private List<T> replaceList;


    public boolean childrenHasChange(){
        return  CollectionUtils.isNotEmpty(removeList)||CollectionUtils.isNotEmpty(addList)||CollectionUtils.isNotEmpty(replaceList);
    }

}
