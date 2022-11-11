package com.horkr.algorithm.base.sort;

public class Bubble implements Sorter<Integer> {

    @Override
    public void arrSort(Integer[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    int t = arr[j];
                    arr[j] = arr[i];
                    arr[i] = t;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Bubble().test(new Integer[]{3, 2, 4, 1, 5});
    }
}
