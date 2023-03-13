package com.horkr.util.tree;

/**
 * @author 卢亮宏
 */
public class Relation<T extends Node> {

    private String id;

    private String fromId;

    private String toId;

    private T childNode;


    public Relation() {
    }

    public Relation(String id, String fromId, String toId, T childNode) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.childNode = childNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public T getChildNode() {
        return childNode;
    }

    public void setChildNode(T childNode) {
        this.childNode = childNode;
    }
}
