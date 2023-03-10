package com.horkr.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 卢亮宏
 */
public class Node<T extends Relation> {

    private String id;

    private List<T> children = new ArrayList<>();


    public void addChildren(Collection<T> newChildren){
        children.addAll(newChildren);
    }

    public void addChild(T newChild){
        children.add(newChild);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public Node() {
    }

    public Node(String id, List<T> children) {
        this.id = id;
        this.children = children;
    }
}
