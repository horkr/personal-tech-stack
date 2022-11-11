package com.horkr.algorithm.base.queue;


//队列需要用循环队列，因为先进先去的特性，如果不用循坏，在入队和出队的过程中会有空间会被浪费
//队列所需参数front，rear，2个参数会在不同场合下有不同的意义
//1.队列初始化：font和rear的值都是0
//2.队列非空：font表示第一个有效元素，rear表示最后一个有效元素的下一个元素
//3.font和rear相等，但不一定为0
//入队时rear向前移动一位，与font无关 r = （r+1）%数组长度，有图解，出队，font向前移动一位，rear不变，道理一样
//当font与rear相等时队列为空
//当（rear+1）%数组长度=font时队列为满,最后会发现rear指向的那个下标总是空的

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Queue {
    //数组长度
    private Integer length;

    public Queue(Integer length) {
        this.length = length;
        array = new Object[length];
    }

    private Object[] array;

    //第一个元素下标
    private int front = 0;

    //最后一个元素
    private int rear = 0;

    public Boolean in_queue(Object data) {
        if (isFull()) {
            System.out.println("当前队列已满：" + front + " " + rear);
            return false;
        }
        if (nonNull(array[rear])) {
            rear = (rear + 1) % length;
        }
        array[rear] = data;
        return true;
    }

    public Object out_queue() {
        if(isNull(array[front])){
            front = (front + 1) % length;
        }
        Object o = array[front];
        array[front] = null;
        return o;
    }

    public Boolean isFull() {
        if ((rear + 1) % length == front) {
            return true;
        }
        return false;
    }

    public Boolean isEmpty() {
        if (rear == front) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Object o : array) {
            builder.append(o + " ");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        Queue queue = new Queue(6);
        queue.in_queue(1);
        queue.in_queue(2);
        queue.in_queue(3);
        queue.in_queue(4);
        queue.in_queue(5);
        queue.in_queue(6);
        System.out.println(queue.toString());
        System.err.println(queue.out_queue());
        queue.in_queue(7);
        System.out.println(queue.toString());
        System.err.println(queue.out_queue());
    }
}
