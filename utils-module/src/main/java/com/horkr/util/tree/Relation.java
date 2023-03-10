package com.horkr.util.tree;

/**
 * @author 卢亮宏
 */
public class Relation<R extends Node> {

    private String id;

    private String fromId;

    private String toId;

    private R childNode;


    public Relation() {
    }

    public Relation(String id, String fromId, String toId, R childNode) {
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

    public R getChildNode() {
        return childNode;
    }

    public void setChildNode(R childNode) {
        this.childNode = childNode;
    }
}
