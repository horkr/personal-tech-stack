package com.horkr.util.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 资源的json显示对象
 */
public class TreeItem implements Comparable<TreeItem>, Serializable, Cloneable {

    private String frontId;

    private String parentFrontId;

    private String ebomFrontId;

    private static final long serialVersionUID = 5416925319097605233L;

    public static class TreeItemState implements Serializable{
        private static final long serialVersionUID = 6614157474326219263L;
        public Boolean opened;
        public Boolean disabled;
        public Boolean  selected;
        public Boolean  highlight;
    }

    private String kbn;
    /**
     * 资源id
     */
    private String id;
    /**
     * 资源id
     */
    private String key;
    /**
     * 父资源id
     */
    private String parentId;
    /**
     * 显示文本
     */
    private String text;
    /**
     * 显示文本
     */
    private String title;

    private String icon;

    /**
     * 子资源数
     */
    private int child;

    private Object data;

    private Integer level;

    private Map<String,String> param;

    /**
     * 节点闭合状态
     */
    private TreeItemState state = new TreeItemState();

    private int order;
    /**
     * 子资源列表
     */
    private List<TreeItem> children = new ArrayList<TreeItem>();

    /**
     * 子资源列表
     */
    private List<TreeItem> item = new ArrayList<TreeItem>();

    public List<TreeItem> getItem() {
        return item;
    }
    public void setItem(List<TreeItem> item) {
        this.item = item;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public TreeItemState getState() {
        return state;
    }
    public void setState(TreeItemState state) {
        this.state = state;
    }
    public List<TreeItem> getChildren() {
        return children;
    }
    public void setChildren(List<TreeItem> children) {
        this.children = children;
    }
    public int getChild() {
        return child;
    }
    public void setChild(int child) {
        this.child = child;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
        setKey(id);
    }
    public String getText() {
        return text;
    }
    public String getLabel() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
        setTitle(text);
    }
    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    @Override
    public int compareTo(TreeItem other) {
        if(other != null){
            return order - other.order;
        }
        return 1;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public String getKbn() {
        return kbn;
    }
    public void setKbn(String kbn) {
        this.kbn = kbn;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public String getFrontId() {
        return frontId;
    }

    public void setFrontId(String frontId) {
        this.frontId = frontId;
    }

    public String getParentFrontId() {
        return parentFrontId;
    }

    public void setParentFrontId(String parentFrontId) {
        this.parentFrontId = parentFrontId;
    }

    public String getEbomFrontId() {
        return ebomFrontId;
    }

    public void setEbomFrontId(String ebomFrontId) {
        this.ebomFrontId = ebomFrontId;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }
}