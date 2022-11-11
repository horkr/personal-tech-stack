package com.horkr.algorithm.base.tree;

public class TreeNode<E> {
    private E data;
    private TreeNode<E> left;
    private TreeNode<E> right;
    private boolean lastChild;

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public TreeNode<E> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<E> left) {
        this.left = left;
    }

    public TreeNode<E> getRight() {
        return right;
    }

    public void setRight(TreeNode<E> right) {
        this.right = right;
    }

    public TreeNode(E data) {
        this.data = data;
    }

    public boolean isLastChild() {
        return lastChild;
    }

    public void setLastChild(boolean lastChild) {
        this.lastChild = lastChild;
    }

    public TreeNode() {
    }
}
