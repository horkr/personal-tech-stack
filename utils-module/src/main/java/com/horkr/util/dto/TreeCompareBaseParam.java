package com.horkr.util.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TreeCompareBaseParam implements Serializable {

    private static final long serialVersionUID = 2605393787744625336L;
    /**
     * 是否对比根节点（如果不对比将没有差异属性）
     */
    private boolean compareRoot;

    /**
     * 单个树节点唯一主键字段，支持内嵌对象，如：obj.obj.prop
     */
    private List<String> uniqueKeyFields;


    /**
     * 单个树节点对比的属性，支持内嵌对象，如：obj.obj.prop
     */
    private List<String> compareProperties;

    /**
     * 唯一主键映射，对比时会将有映射关系的主键视为同一主键
      */
    private Map<String,String> uniqueKeyMapping = new HashMap<>();



    protected void checkConfig(Object config, String key) {
        Objects.requireNonNull(config,String.format("the config in TreeCompareParam [%s] can not null",key));
    }

    protected void check() {
        checkConfig(uniqueKeyFields,"uniqueKeyFields");
        checkConfig(compareProperties,"compareProperties");
    }


    public boolean isCompareRoot() {
        return compareRoot;
    }

    public void setCompareRoot(boolean compareRoot) {
        this.compareRoot = compareRoot;
    }

    public List<String> getUniqueKeyFields() {
        return uniqueKeyFields;
    }

    public void setUniqueKeyFields(List<String> uniqueKeyFields) {
        this.uniqueKeyFields = uniqueKeyFields;
    }

    public List<String> getCompareProperties() {
        return compareProperties;
    }

    public void setCompareProperties(List<String> compareProperties) {
        this.compareProperties = compareProperties;
    }

    public Map<String, String> getUniqueKeyMapping() {
        return uniqueKeyMapping;
    }

    public void setUniqueKeyMapping(Map<String, String> uniqueKeyMapping) {
        this.uniqueKeyMapping = uniqueKeyMapping;
    }
}
