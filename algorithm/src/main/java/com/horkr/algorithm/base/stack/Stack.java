package com.horkr.algorithm.base.stack;

import java.util.ArrayList;
import java.util.List;

/*
 * 先进后出，ptop始终指向最顶部的有效元素，pbuttom指向最底部无效元素，本质是链表
 * */
public class Stack<E> {
    /**
     * 栈中的节点
     *
     * @param <E> 节点中数据类型
     */
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }

        Node() {
        }

        E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }

        Node<E> getNext() {
            return next;
        }

        void setNext(Node<E> next) {
            this.next = next;
        }

        public Boolean equals(Node<E> node) {
            return node.getNext() == getNext() && node.getData() == getData();
        }
    }

    private Node<E> pButtom = new Node<>();
    private Node<E> pTop = pButtom;

    /**
     * 往top上部放
     *
     * @param val 节点值
     */
    public void push(E val) {
        Node<E> node = new Node<E>(val);
        pTop.setNext(node);
        node.setNext(null);
        pTop = node;
    }

    /**
     * 后进先出原则,取顶节点数据并将顶点向下移动一位
     * @return  E
     */
    public E pop() {
        Node<E> node = pButtom;
        while (!node.getNext().equals(pTop)) {
            node = node.getNext();
        }
        E data = pTop.getData();
        node.setNext(null);
        pTop = node;
        return data;
    }

    public Boolean isEmpty() {
        return pButtom.equals(pTop);
    }


    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        List<Integer> list = new ArrayList<Integer>();
        list.add(stack.pop());
        list.add(stack.pop());
        list.add(stack.pop());
        list.add(stack.pop());
        System.out.println(list);

    }
}
