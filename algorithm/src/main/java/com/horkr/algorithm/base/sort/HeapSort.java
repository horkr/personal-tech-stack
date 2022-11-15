package com.horkr.algorithm.base.sort;

import com.horkr.algorithm.base.heap.MaxHeap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 卢亮宏
 */
public class HeapSort {
    private static final Logger log = LogManager.getLogger(HeapSort.class);

    private static void sortWithBaseHeap() {
        Integer[] arr = {3, 2, 4, 1, 5};
        MaxHeap<Integer> maxHeap = new MaxHeap<>(arr);
        // 取出来的顺序应该是按照从大到小的顺序取出来的
        for (int i = 0; i < maxHeap.getCapacity(); i++) {
            arr[i] = maxHeap.extractTop();
            log.info("data:{}", arr[i]);
        }
    }

    /**
     * 优化的堆排序
     */
    private static void optimizedSortWithHeap() {
        Integer[] arr = {3, 2, 4, 1, 5};
        sort(arr);
        for (Integer integer : arr) {
            log.info(integer);
        }
    }

    public static void sort(Comparable[] arr) {

        int n = arr.length;

        // 注意，此时我们的堆是从0开始索引的
        // 从(最后一个元素的索引-1)/2开始
        // 最后一个元素的索引 = n-1
        for (int i = (n - 1 - 1) / 2; i >= 0; i--)
            shiftDown(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            shiftDown(arr, i, 0);
        }
    }

    // 交换堆中索引为i和j的两个元素
    private static void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    // 原始的shiftDown过程
    private static void shiftDown(Comparable[] arr, int n, int k) {

        while (2 * k + 1 < n) {
            //左孩子节点
            int j = 2 * k + 1;
            //右孩子节点比左孩子节点大
            if (j + 1 < n && arr[j + 1].compareTo(arr[j]) > 0)
                j += 1;
            //比两孩子节点都大
            if (arr[k].compareTo(arr[j]) >= 0) break;
            //交换原节点和孩子节点的值
            swap(arr, k, j);
            k = j;
        }
    }

    public static void main(String[] args) {
        optimizedSortWithHeap();
    }
}
