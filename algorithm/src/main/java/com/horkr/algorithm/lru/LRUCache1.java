package com.horkr.algorithm.lru;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * 首先要接收一个 capacity 参数作为缓存的最大容量，然后实现两个 API，一个是 put(key, val) 方法存入键值对，另一个是 get(key) 方法获取 key 对应的 val，
 * 如果 key 不存在则返回 -1。
 * 注意哦，get 和 put 方法必须都是 O(1) 的时间复杂度
 * @author 卢亮宏
 */
public class LRUCache1<K,V> {

    private Node<K,V> head;

    private Node<K,V> tail;

    private Map<K,Node<K,V>> map = new HashMap<>();

    private int cap;

    public void put(K key,V val){
        if(isNull(head)&&isNull(tail)){
            head = new Node<>();
            head.setKey(key);
            head.setVal(val);
            tail = head;
            return;
        }
        Node<K, V> newNode = new Node<>(key, val);
        link2Head(newNode);
        map.put(key,head);
        if(map.size()>cap){
            Node<K, V> tailPre = tail.getPre();
        }
    }



    private void link2Head(Node<K, V> newNode){
        newNode.setNext(head);
        head.setPre(newNode);
        head = newNode;
    }
}
