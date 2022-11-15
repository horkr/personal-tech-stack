package com.horkr.algorithm.base.heap;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * 详细文档见：https://jcjspmsqiu.feishu.cn/wiki/wikcnLyeRPUekDWrwlCEXCLhzMb
 *
 * @author 卢亮宏
 */
public class MaxHeap<T extends Comparable<T>> {

    /**
     * 存储数组,0位不存储数据
     */
    private T[] data;


    /**
     * 当前元素总数
     */
    private int size;

    /**
     * 数组容量
     */
    private int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        data = (T[]) new Comparable[capacity + 1];
    }

    public MaxHeap(T[] existArr) {
        capacity = existArr.length;
        data = (T[]) new Comparable[capacity + 1];
        for (int i = 0; i < capacity; i++) {
            data[i + 1] = existArr[i];
        }
        size = existArr.length;
        // 调整数组位置，从第一个不是叶子节点的元素开始进行下移
        for (int i = size / 2; i >=1 ; i--) {
            shiftDown(i);
        }
    }


    /**
     * 父节点的索引位置总是index/2
     *
     * @param itemIndex 当前节点
     * @return int
     */
    private int parentIndex(int itemIndex) {
        return itemIndex / 2;
    }

    /**
     * @param itemIndex 当前节点
     * @return int
     */
    private int leftChildIndex(int itemIndex) {
        return itemIndex * 2;
    }


    /**
     * @param itemIndex 当前节点
     * @return int
     */
    private int rightChildIndex(int itemIndex) {
        return itemIndex * 2 + 1;
    }

    /**
     * 插入新元素
     *
     * @param item item
     */
    public void insert(T item) {
        requireNonNull(item);
        if (size >= capacity) {
            grow();
        }
        data[size + 1] = item;
        size++;
        shiftUp(size);
    }

    /**
     * 移除堆顶部元素
     */
    public T extractTop() {
        if (size <= 0) {
            return null;
        }
        T item = data[1];
        swap(1, size);
        size--;
        shiftDown(1);
        return item;
    }

    /**
     * 扩容
     */
    public void grow() {
        capacity = capacity * 2;
        data = Arrays.copyOf(data, capacity);
    }


    /**
     * 上移，当前索引元素和父索引位置互换元素
     *
     * @param index 索引
     */
    public void shiftUp(int index) {
        // 操作索引大于1（等于1时已经上移到堆顶了），且索引位置值，大于父位置值是，上移元素
        while (index > 1 && data[parentIndex(index)].compareTo(data[index]) < 0) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }


    /**
     * 下移
     *
     * @param index 索引
     */
    public void shiftDown(int index) {
        // 当左边子节点小于，总元素个数，大于时已经没法再下移了
        while (leftChildIndex(index) < size) {
            int leftChildIndex = leftChildIndex(index);
            int rightChildIndex = rightChildIndex(index);
            int swapIndex = leftChildIndex;
            if (rightChildIndex <= size && data[rightChildIndex].compareTo(data[swapIndex]) > 0) {
                swapIndex = rightChildIndex;
            }
            // 已经比子节点中最大的都要大了，不需要再互换位置
            if (data[index].compareTo(data[swapIndex]) > 0) {
                break;
            }
            swap(swapIndex, index);
            index = swapIndex;
        }
    }


    /**
     * 两个索引位置互换数据
     *
     * @param i i
     * @param j j
     */
    private void swap(int i, int j) {
        T item = data[i];
        data[i] = data[j];
        data[j] = item;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }
}
