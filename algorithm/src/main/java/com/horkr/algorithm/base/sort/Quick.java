package com.horkr.algorithm.base.sort;


import com.horkr.algorithm.base.stack.Stack;

/**
 * 快速排序
 */
public class Quick implements Sorter<Integer> {

    @Override
    public void arrSort(Integer[] arr) {
        recursion(arr, 0, arr.length - 1);
    }

    /**
     * 递归模式
     *
     * @param arr arr
     */
    public void recursion(Integer[] arr, int low, int high) {
        // 递归结束条件
        if (high > low) {
            // 找到数组第一个元素应该在的位置pos，并将arr[pos]赋上此值.利用pos将数组分为俩半，再继续递归
            int pos = findPos(arr, low, high);
            recursion(arr, 0, pos - 1);
            recursion(arr, pos + 1, high);
        }
    }

    /**
     * 非递归  借助栈模式
     */
    public void stack(Integer arr[],int low,int high) {
        int pos;
        if (low >= high)
            return;
        Stack<Integer> stack = new Stack<>();
        stack.push(low);
        stack.push(high);
        while (!stack.isEmpty()) {
            // 先弹出high,再弹出low
            high = stack.pop();
            low = stack.pop();
            pos = findPos(arr, low, high);
            // 先压low,再压high
            if (low < pos - 1) {
                stack.push(low);
                stack.push(pos - 1);
            }
            if (pos + 1 < high) {
                stack.push(pos + 1);
                stack.push(high);
            }
        }
    }

    /**
     * 查找数组第一个元素位置
     *
     * @return int
     */
    public int findPos(Integer[] arr, int low, int high) {
        Integer firstValue = arr[low];
        while (high > low) {
            //1.从high->low方向找到第一个小于firstValue的值且替换
            while (high > low && arr[high] >= firstValue) {
                high--;
            }
            // 条件破坏后替换
            arr[low] = arr[high];

            //2.从low->high方向找到第一个大于firstValue的值且替换
            while (high > low && arr[low] <= firstValue) {
                low++;
            }
            // 条件破坏后替换
            arr[high] = arr[low];
        }
        arr[low] = firstValue;
        return low;
    }

    public static void main(String[] args) {
        Integer[] arr = {9, 0, 8, 10, -5, 2, 13, 7};
        new Quick().test(arr);
    }
}
