package com.horkr.algorithm.base.tree;

public class RbTree<T> {
    private Node root = new Node();



    class Node {
        private Node right;
        private Node left;
        private T data;
        private Node parent;

        Node() {
        }

        Node(T data) {
            this.data = data;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }
}
