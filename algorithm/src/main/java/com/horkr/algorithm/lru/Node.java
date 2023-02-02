package com.horkr.algorithm.lru;

/**
 * @author 卢亮宏
 */
public class Node<K,V> {

    private K key;

    private V val;

    private Node<K,V> pre;

    private Node<K,V> next;

    public Node() {
    }

    public Node(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getVal() {
        return val;
    }

    public void setVal(V val) {
        this.val = val;
    }

    public Node<K, V> getPre() {
        return pre;
    }

    public void setPre(Node<K, V> pre) {
        this.pre = pre;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }
}
