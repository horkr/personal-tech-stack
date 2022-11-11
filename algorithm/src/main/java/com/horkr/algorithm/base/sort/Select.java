package com.horkr.algorithm.base.sort;

public class Select implements Sorter<Integer> {
    @Override
    public void arrSort(Integer[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            if(min!=i){
                int temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }
        }
    }

    public static void main(String[] args) {
        new Select().test(new Integer[]{3, 2, 4, 1, 5});
    }
}
