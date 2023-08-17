package com.example.sort;

import java.util.Arrays;

public interface Sort {
    int[] sort(int[] targetArray);

    /**
     * 测试
     *
     * @param targetArray 待排序数组
     */
    default void test(int[] targetArray) {
        System.out.println("待排序数组为:");
        System.out.println(Arrays.toString(targetArray));
        System.out.println("排序后数组为:");
        System.out.println(Arrays.toString(this.sort(targetArray)));
        System.out.println("排序过程");
        System.out.println(PROCESS_BUILDER);
    }

    default void test() {
        test(DEFAULT_TEST_ARRAY);
    }

    /**
     * 交换数组中的两个元素(index1位置的元素和index2位置的元素交换)
     * @param array 数组
     * @param index1 下标1
     * @param index2 下标2
     */
    default void exchange(int[] array, int index1, int index2) {
        if(index1 == index2) {
            return;
        }
        array[index1] = array[index1] + array[index2];
        array[index2] = array[index1] - array[index2];
        array[index1] = array[index1] - array[index2];
    }

    int[] DEFAULT_TEST_ARRAY= new int[] {12, 1000, 333, 11, 2, 3, 1, 0, 9, 8, 11, 44, 56};
    StringBuilder PROCESS_BUILDER = new StringBuilder();
}
