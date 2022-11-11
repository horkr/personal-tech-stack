package com.horkr.util.dto;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 两个list对比结果
 * @author llh
 */
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

    public List<T> getAddList() {
        return addList;
    }

    public void setAddList(List<T> addList) {
        this.addList = addList;
    }

    public List<T> getRemoveList() {
        return removeList;
    }

    public void setRemoveList(List<T> removeList) {
        this.removeList = removeList;
    }

    public List<T> getNoChangeList() {
        return noChangeList;
    }

    public void setNoChangeList(List<T> noChangeList) {
        this.noChangeList = noChangeList;
    }

    public List<T> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<T> changeList) {
        this.changeList = changeList;
    }

    public List<T> getReplaceList() {
        return replaceList;
    }

    public void setReplaceList(List<T> replaceList) {
        this.replaceList = replaceList;
    }

    public ListCompareResult(List<T> addList, List<T> removeList, List<T> noChangeList, List<T> changeList, List<T> replaceList) {
        this.addList = addList;
        this.removeList = removeList;
        this.noChangeList = noChangeList;
        this.changeList = changeList;
        this.replaceList = replaceList;
    }


    public ListCompareResult() {
    }

    public boolean childrenHasChange(){
        return  CollectionUtils.isNotEmpty(removeList)||CollectionUtils.isNotEmpty(addList)||CollectionUtils.isNotEmpty(replaceList);
    }


    public static void main(String[] args) {
        ListCompareResult<Object> objectListCompareResult = new ListCompareResult<>();
        System.out.println(objectListCompareResult.getAddList());
    }

}
