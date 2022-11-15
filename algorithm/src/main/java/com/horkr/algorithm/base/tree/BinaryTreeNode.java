package com.horkr.algorithm.base.tree;

public class BinaryTreeNode<E> {
    /**
     * 节点存储数据
     */
    private E data;

    /**
     * 坐节点
     */
    private BinaryTreeNode<E> left;

    /**
     * 右节点
     */
    private BinaryTreeNode<E> right;

    /**
     * 是否为最后一个节点
     */
    private boolean lastChild;

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public BinaryTreeNode<E> getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeNode<E> left) {
        this.left = left;
    }

    public BinaryTreeNode<E> getRight() {
        return right;
    }

    public void setRight(BinaryTreeNode<E> right) {
        this.right = right;
    }

    public BinaryTreeNode(E data) {
        this.data = data;
    }

    public boolean isLastChild() {
        return lastChild;
    }

    public void setLastChild(boolean lastChild) {
        this.lastChild = lastChild;
    }

    public BinaryTreeNode() {
    }
}
