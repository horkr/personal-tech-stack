package com.horkr.algorithm.base.sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 排序器
 */
public interface Sorter<T> {


    /**
     * 输出数组
     *
     * @param arr arr
     */
    default void print(T[] arr) {
        System.out.println(Arrays.stream(arr).map(String::valueOf).collect(Collectors.joining(",")));
    }

    /**
     * 数组排序
     *
     * @param arr arr
     */
    void arrSort(T[] arr);

    default void test(T[] arr) {
        arrSort(arr);
        print(arr);
    }


}
